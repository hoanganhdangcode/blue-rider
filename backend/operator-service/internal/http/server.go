package httpserver

import (
	"context"
	"encoding/json"
	"net/http"

	"github.com/go-chi/chi/v5"
	"github.com/redis/go-redis/v9"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/readpref"

	"github.com/blueride/operator-service/internal/config"
	"github.com/blueride/operator-service/internal/kafka"
)

type Server struct {
	cfg    config.Config
	mongo  *mongo.Client
	redis  *redis.Client
	kafka  *kafka.Producer
}

func New(cfg config.Config, mongoClient *mongo.Client, redisClient *redis.Client, producer *kafka.Producer) http.Handler {
	s := &Server{cfg: cfg, mongo: mongoClient, redis: redisClient, kafka: producer}
	r := chi.NewRouter()
	r.Get("/health", s.health)
	r.Group(func(r chi.Router) {
		r.Use(s.requireInternalKey)
		r.Get("/internal/v1/ping", s.ping)
	})
	return r
}

func (s *Server) requireInternalKey(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if r.Header.Get("X-Internal-Key") != s.cfg.InternalAPIKey {
			writeJSON(w, http.StatusUnauthorized, map[string]string{"error": "unauthorized"})
			return
		}
		next.ServeHTTP(w, r)
	})
}

func (s *Server) ping(w http.ResponseWriter, _ *http.Request) {
	writeJSON(w, http.StatusOK, map[string]string{"service": "operator-service"})
}

func (s *Server) health(w http.ResponseWriter, r *http.Request) {
	status := map[string]string{
		"status":  "ok",
		"service": "operator-service",
		"mongo":   s.mongoStatus(r.Context()),
		"redis":   s.redisStatus(r.Context()),
		"kafka":   s.kafkaStatus(r.Context()),
	}
	code := http.StatusOK
	for _, key := range []string{"mongo", "redis", "kafka"} {
		if status[key] != "ok" {
			status["status"] = "degraded"
			code = http.StatusServiceUnavailable
			break
		}
	}
	writeJSON(w, code, status)
}

func (s *Server) mongoStatus(ctx context.Context) string {
	if s.mongo == nil {
		return "down"
	}
	if err := s.mongo.Ping(ctx, readpref.Primary()); err != nil {
		return "down"
	}
	return "ok"
}

func (s *Server) redisStatus(ctx context.Context) string {
	if s.redis == nil {
		return "down"
	}
	if err := s.redis.Ping(ctx).Err(); err != nil {
		return "down"
	}
	return "ok"
}

func (s *Server) kafkaStatus(ctx context.Context) string {
	if err := kafka.Ping(ctx, s.cfg.KafkaBrokers, s.cfg.ConnectTimeout); err != nil {
		return "down"
	}
	return "ok"
}

func writeJSON(w http.ResponseWriter, status int, body any) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	_ = json.NewEncoder(w).Encode(body)
}
