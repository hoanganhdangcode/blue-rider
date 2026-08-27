package httpserver

import (
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/blueride/operator-service/internal/config"
)

func TestPingRequiresInternalKey(t *testing.T) {
	handler := New(config.Config{InternalAPIKey: "dev-internal-key"}, nil, nil, nil)
	req := httptest.NewRequest(http.MethodGet, "/internal/v1/ping", nil)
	rec := httptest.NewRecorder()
	handler.ServeHTTP(rec, req)
	if rec.Code != http.StatusUnauthorized {
		t.Fatalf("expected 401, got %d", rec.Code)
	}
}

func TestPingOK(t *testing.T) {
	handler := New(config.Config{InternalAPIKey: "dev-internal-key"}, nil, nil, nil)
	req := httptest.NewRequest(http.MethodGet, "/internal/v1/ping", nil)
	req.Header.Set("X-Internal-Key", "dev-internal-key")
	rec := httptest.NewRecorder()
	handler.ServeHTTP(rec, req)
	if rec.Code != http.StatusOK {
		t.Fatalf("expected 200, got %d", rec.Code)
	}
	var body map[string]string
	if err := json.Unmarshal(rec.Body.Bytes(), &body); err != nil {
		t.Fatal(err)
	}
	if body["service"] != "operator-service" {
		t.Fatalf("unexpected body: %v", body)
	}
}
