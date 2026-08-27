package config

import "testing"

func TestSplitBrokers(t *testing.T) {
	got := split("kafka:9092, kafka-2:9092")
	if len(got) != 2 || got[0] != "kafka:9092" || got[1] != "kafka-2:9092" {
		t.Fatalf("unexpected brokers: %#v", got)
	}
}
