package kafka

import (
	"context"
	"fmt"
	"net"
	"time"

	kafkago "github.com/segmentio/kafka-go"
)

type Producer struct {
	Writer *kafkago.Writer
}

func Connect(ctx context.Context, brokers []string, timeout time.Duration) (*Producer, error) {
	if len(brokers) == 0 {
		return nil, fmt.Errorf("kafka brokers are empty")
	}
	dialer := &kafkago.Dialer{Timeout: timeout}
	conn, err := dialer.DialContext(ctx, "tcp", brokers[0])
	if err != nil {
		return nil, fmt.Errorf("kafka dial: %w", err)
	}
	_ = conn.Close()
	return &Producer{
		Writer: &kafkago.Writer{
			Addr:         kafkago.TCP(brokers...),
			Balancer:     &kafkago.LeastBytes{},
			RequiredAcks: kafkago.RequireAll,
			BatchTimeout: 10 * time.Millisecond,
		},
	}, nil
}

func (p *Producer) Close() error {
	if p == nil || p.Writer == nil {
		return nil
	}
	return p.Writer.Close()
}

func Ping(ctx context.Context, brokers []string, timeout time.Duration) error {
	if len(brokers) == 0 {
		return fmt.Errorf("kafka brokers are empty")
	}
	d := net.Dialer{Timeout: timeout}
	conn, err := d.DialContext(ctx, "tcp", brokers[0])
	if err != nil {
		return err
	}
	return conn.Close()
}
