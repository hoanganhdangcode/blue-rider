package config

import (
	"os"
	"strings"
	"time"
)

type Config struct {
	HTTPAddr         string
	MongoURI         string
	MongoDB          string
	RedisAddr        string
	KafkaBrokers     []string
	CoreBaseURL      string
	InternalAPIKey   string
	ConnectTimeout   time.Duration
	StartupRetries   int
	StartupRetryWait time.Duration
}

func Load() Config {
	return Config{
		HTTPAddr:         env("HTTP_ADDR", ":8080"),
		MongoURI:         env("MONGO_URI", "mongodb://localhost:27017"),
		MongoDB:          env("MONGO_DB", "blue_ride_operator"),
		RedisAddr:        env("REDIS_ADDR", "localhost:6379"),
		KafkaBrokers:     split(env("KAFKA_BROKERS", "localhost:9092")),
		CoreBaseURL:      strings.TrimRight(env("CORE_BASE_URL", "http://localhost:8080"), "/"),
		InternalAPIKey:   env("INTERNAL_API_KEY", "dev-internal-key"),
		ConnectTimeout:   5 * time.Second,
		StartupRetries:   15,
		StartupRetryWait: 2 * time.Second,
	}
}

func env(key, fallback string) string {
	if value := strings.TrimSpace(os.Getenv(key)); value != "" {
		return value
	}
	return fallback
}

func split(value string) []string {
	parts := strings.Split(value, ",")
	out := make([]string, 0, len(parts))
	for _, part := range parts {
		part = strings.TrimSpace(part)
		if part != "" {
			out = append(out, part)
		}
	}
	return out
}
