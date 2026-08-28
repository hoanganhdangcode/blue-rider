# Blue-Ride

Nền tảng gọi xe / giao hàng (kiểu Grab).

## Tài liệu

| File | Nội dung |
|---|---|
| [docs/BACKEND_GUIDE.md](docs/BACKEND_GUIDE.md) | **Hướng dẫn tiếng Việt**: kiến trúc, cấu trúc code, build, chạy local, deploy |
| [docs/PROJECT_CONTEXT.md](docs/PROJECT_CONTEXT.md) | Nguồn chân lý kiến trúc backend (ngắn) |
| [docs/COMMIT_CONVENTION.md](docs/COMMIT_CONVENTION.md) | Conventional Commits |
| [AGENTS.md](AGENTS.md) | Quy tắc bắt buộc cho contributor / AI |

## Branch = environment

| Branch | Environment | Backend chạy như thế nào |
|---|---|---|
| `main` | Production | Docker Compose + Nginx (profile `nginx`), CI/CD `deploy.yml` |
| `develop` | Test / Integration (staging) | Docker Compose + Nginx (profile `nginx`), CI `*-ci.yml` + `deploy.yml` |
| `features` | Local development | Docker Compose **không Nginx**, gateway-service publish thẳng ra LAN, không GitHub Actions |

`infra/docker/compose.yml` là **một nguồn cấu hình duy nhất** cho cả 3 môi trường; khác biệt chỉ nằm ở việc có bật profile `nginx` (test/production) hay có nạp thêm override `compose.local.yml` (local) hay không. Biến môi trường (`infra/.env`, không commit) là nơi khác biệt còn lại — xem `infra/.env.example`.

## Backend — full stack (như test/staging, có Nginx)

```bash
# Build & test
cd backend
./gradlew check   # Windows: gradlew.bat check

# Chạy đủ stack qua Nginx (giống develop/main)
cd ../infra
cp .env.example .env
cd docker
docker compose --env-file ../.env -f compose.yml --profile nginx up -d --build

# Smoke test
curl -s -X POST http://localhost/api -H "X-API-ACTION: auth.ping" -H "Content-Type: application/json"
curl -s -X POST http://localhost/api -H "X-API-ACTION: core.ping" -H "Content-Type: application/json"
```

Ba process chạy: **gateway-service**, **auth-service**, **core-service**. Chi tiết trong [BACKEND_GUIDE.md](docs/BACKEND_GUIDE.md).

## Local development (branch `features`) — không Nginx, truy cập qua LAN

```bash
git checkout features

cd infra
cp .env.example .env    # chỉnh nếu cần, không commit .env

cd docker
docker compose --env-file ../.env -f compose.yml -f compose.local.yml up -d --build

# Tìm LAN IP của máy + các lệnh mẫu tiếp theo
../scripts/dev-ip.sh
```

`compose.local.yml` publish thẳng `gateway-service` ra host trên tất cả interface (không qua Nginx), nên thiết bị Android cùng LAN gọi được `http://<LAN_IP>:8080` — port lấy từ biến `GATEWAY_PORT` trong `infra/.env` (mặc định `8080`), đổi LAN IP không cần sửa code vì Docker tự publish trên mọi interface. `auth-service`, `core-service`, Postgres/Mongo/Redis/Kafka vẫn chỉ nội bộ Docker network hoặc `127.0.0.1`, không lộ ra LAN.

Trỏ app Flutter (`frontend/appmobile`) vào backend LAN:

```bash
cd frontend/appmobile/apps/app_a   # hoặc app_b
flutter run --dart-define=API_BASE_URL=http://<LAN_IP>:8080
```

Chi tiết đầy đủ (ports, biến môi trường, cách thêm API): [BACKEND_GUIDE.md](docs/BACKEND_GUIDE.md).

**`features` không có GitHub Actions / CI-CD** — các workflow trong `.github/workflows/` chỉ trigger trên `develop` và `main`.
