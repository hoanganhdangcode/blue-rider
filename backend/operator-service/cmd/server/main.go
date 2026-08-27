package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/blueride/operator-service/internal/config"
	"github.com/blueride/operator-service/internal/coreclient"
	httpserver "github.com/blueride/operator-service/internal/http"
	kafkax "github.com/blueride/operator-service/internal/kafka"
	mongox "github.com/blueride/operator-service/internal/mongo"
	redisx "github.com/blueride/operator-service/internal/redis"
	"github.com/redis/go-redis/v9"
	"go.mongodb.org/mongo-driver/mongo"
)

func main() {
	cfg := config.Load()
	ctx := context.Background()

	mongoClient, err := connectMongo(ctx, cfg)
	if err != nil {
		log.Fatalf("mongo: %v", err)
	}
	defer func() {
		_ = mongoClient.Disconnect(context.Background())
	}()

	redisClient, err := connectRedis(ctx, cfg)
	if err != nil {
		log.Fatalf("redis: %v", err)
	}
	defer func() {
		_ = redisClient.Close()
	}()

	producer, err := connectKafka(ctx, cfg)
	if err != nil {
		log.Fatalf("kafka: %v", err)
	}
	defer func() {
		_ = producer.Close()
	}()

	_ = coreclient.New(cfg.CoreBaseURL, cfg.InternalAPIKey, cfg.ConnectTimeout)

	server := &http.Server{
		Addr:              cfg.HTTPAddr,
		Handler:           httpserver.New(cfg, mongoClient, redisClient, producer),
		ReadHeaderTimeout: 5 * time.Second,
	}

	go func() {
		log.Printf("operator-service listening on %s", cfg.HTTPAddr)
		if err := server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			log.Fatalf("http: %v", err)
		}
	}()

	stop := make(chan os.Signal, 1)
	signal.Notify(stop, syscall.SIGINT, syscall.SIGTERM)
	<-stop

	shutdownCtx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()
	_ = server.Shutdown(shutdownCtx)
}

func connectMongo(ctx context.Context, cfg config.Config) (*mongo.Client, error) {
	var last error
	for i := 0; i < cfg.StartupRetries; i++ {
		client, _, err := mongox.Connect(ctx, cfg.MongoURI, cfg.MongoDB, cfg.ConnectTimeout)
		if err == nil {
			log.Printf("mongo connected")
			return client, nil
		}
		last = err
		log.Printf("mongo not ready (%d/%d): %v", i+1, cfg.StartupRetries, err)
		time.Sleep(cfg.StartupRetryWait)
	}
	return nil, last
}

func connectRedis(ctx context.Context, cfg config.Config) (*redis.Client, error) {
	var last error
	for i := 0; i < cfg.StartupRetries; i++ {
		client, err := redisx.Connect(ctx, cfg.RedisAddr, cfg.ConnectTimeout)
		if err == nil {
			log.Printf("redis connected")
			return client, nil
		}
		last = err
		log.Printf("redis not ready (%d/%d): %v", i+1, cfg.StartupRetries, err)
		time.Sleep(cfg.StartupRetryWait)
	}
	return nil, last
}

func connectKafka(ctx context.Context, cfg config.Config) (*kafkax.Producer, error) {
	var last error
	for i := 0; i < cfg.StartupRetries; i++ {
		producer, err := kafkax.Connect(ctx, cfg.KafkaBrokers, cfg.ConnectTimeout)
		if err == nil {
			log.Printf("kafka connected")
			return producer, nil
		}
		last = err
		log.Printf("kafka not ready (%d/%d): %v", i+1, cfg.StartupRetries, err)
		time.Sleep(cfg.StartupRetryWait)
	}
	return nil, last
}
