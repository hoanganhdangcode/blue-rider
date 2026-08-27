# Blue-Ride

Nền tảng gọi xe / giao hàng (kiểu Grab).

## Tài liệu

| File | Nội dung |
|---|---|
| [docs/BACKEND_GUIDE.md](docs/BACKEND_GUIDE.md) | **Hướng dẫn tiếng Việt**: kiến trúc, cấu trúc code, build, chạy local, deploy |
| [docs/PROJECT_CONTEXT.md](docs/PROJECT_CONTEXT.md) | Nguồn chân lý kiến trúc backend (ngắn) |
| [docs/COMMIT_CONVENTION.md](docs/COMMIT_CONVENTION.md) | Conventional Commits |
| [AGENTS.md](AGENTS.md) | Quy tắc bắt buộc cho contributor / AI |

## Backend nhanh

```bash
# Build & test
cd backend
./gradlew check   # Windows: gradlew.bat check

# Chạy đủ stack
cd ../infra
cp .env.example .env
cd docker
docker compose --env-file ../.env up -d --build

# Smoke test
curl -s -X POST http://localhost/api -H "X-API-ACTION: auth.ping" -H "Content-Type: application/json"
curl -s -X POST http://localhost/api -H "X-API-ACTION: core.ping" -H "Content-Type: application/json"
```

Ba process chạy: **gateway-service**, **auth-service**, **core-service**. Chi tiết trong [BACKEND_GUIDE.md](docs/BACKEND_GUIDE.md).
