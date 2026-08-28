# Hướng dẫn Backend Blue-Ride

Tài liệu này giải thích **kiến trúc**, **cấu trúc code**, **cách build/chạy local**, và **cách triển khai** backend Java của Blue-Ride. Đọc kèm [`PROJECT_CONTEXT.md`](PROJECT_CONTEXT.md) (nguồn chân lý kiến trúc) và [`COMMIT_CONVENTION.md`](COMMIT_CONVENTION.md) (quy ước commit).

---

## Mục lục

1. [Tổng quan hệ thống](#1-tổng-quan-hệ-thống)
2. [Cấu trúc thư mục](#2-cấu-trúc-thư-mục)
3. [Vai trò từng module](#3-vai-trò-từng-module)
4. [Luồng request (API công khai)](#4-luồng-request-api-công-khai)
5. [Quy ước viết code trong từng service](#5-quy-ước-viết-code-trong-từng-service)
6. [Yêu cầu môi trường](#6-yêu-cầu-môi-trường)
7. [Build & chạy test](#7-build--chạy-test)
8. [Chạy local bằng Docker Compose](#8-chạy-local-bằng-docker-compose)
9. [Biến môi trường](#9-biến-môi-trường)
10. [Cách thêm API mới (action)](#10-cách-thêm-api-mới-action)
11. [Triển khai (Deploy)](#11-triển-khai-deploy)
12. [CI/CD](#12-cicd)
13. [Checklist & xử lý lỗi thường gặp](#13-checklist--xử-lý-lỗi-thường-gặp)

---

## 1. Tổng quan hệ thống

Blue-Ride là nền tảng gọi xe / giao hàng kiểu Grab. Backend **không** để client gọi thẳng từng service. Client chỉ thấy **một** endpoint công khai.

```
Client (app / web)
    │  POST /api   + header X-API-ACTION
    ▼
Nginx (port 80)
    │  proxy /api
    ▼
gateway-service          ← chỉ định tuyến, bọc envelope, gắn X-Internal-Key
    ├── auth.*  ──────► auth-service  (PostgreSQL)
    └── core.*  ──────► core-service  (MongoDB, Redis, Kafka)
```

| Process chạy thật | Stack | Chịu trách nhiệm | Không được làm |
|---|---|---|---|
| `gateway-service` | Java 21, Spring Boot | `POST /api`, tra `actions.yml`, forward, envelope | Business logic, database |
| `auth-service` | Java 21, Spring Boot, PostgreSQL, JPA, Flyway | User, đăng nhập/đăng ký (sau này), hồ sơ tài xế/xe (dữ liệu ACID) | Matching chuyến, GPS realtime |
| `core-service` | Java 21, Spring Boot, MongoDB, Redis, Kafka | Chuyến đi, matching, giao hàng, vị trí tài xế | Hash password, ký JWT |

Ngoài ra còn module **`backend/crypto`**: đây là **thư viện JAR**, không phải service. Không có port, không có container. Auth (và sau này gateway) nhúng thư viện này để dùng Argon2id / JWT-RSA.

### Nguyên tắc bắt buộc

1. **Không share database** giữa các service. Auth dùng Postgres; core dùng Mongo/Redis. Muốn dữ liệu của nhau → gọi internal REST hoặc Kafka.
2. Client **không** gọi `auth-service` / `core-service` trực tiếp.
3. Crypto (hash mật khẩu, ký JWT) chỉ nằm trong `backend/crypto`, không copy vào `core-service`.
4. Ứng dụng **stateless** — scale ngang bằng nhiều replica + Kafka consumer group.

---

## 2. Cấu trúc thư mục

```
blue-ride/
├── AGENTS.md                      # Quy tắc cho AI / contributor
├── docs/
│   ├── PROJECT_CONTEXT.md         # Kiến trúc (tiếng Anh, ngắn gọn)
│   ├── BACKEND_GUIDE.md           # File này
│   └── COMMIT_CONVENTION.md
├── backend/                       # Monorepo Gradle
│   ├── settings.gradle.kts        # Khai báo module
│   ├── build.gradle.kts           # Java 21, Checkstyle chung
│   ├── gradle.properties
│   ├── gradlew / gradlew.bat      # Gradle Wrapper
│   ├── config/checkstyle/         # Quy tắc lint Java
│   ├── crypto/                    # Thư viện (không chạy độc lập)
│   ├── auth-service/
│   ├── core-service/
│   └── gateway-service/
├── infra/
│   ├── .env.example               # Mẫu biến môi trường
│   ├── docker/compose.yml         # Stack local / VPS
│   └── nginx/nginx.conf
└── .github/workflows/             # CI từng service + Docker + Deploy
```

Mỗi service Spring Boot có layout chuẩn:

```
auth-service/
├── Dockerfile
├── build.gradle.kts
└── src/
    ├── main/
    │   ├── java/com/blueride/auth/
    │   │   ├── AuthApplication.java
    │   │   ├── config/            # Filter internal key, bean crypto, properties
    │   │   ├── health/            # GET /health
    │   │   └── internal/          # API nội bộ /internal/v1/...
    │   └── resources/
    │       ├── application.yml
    │       └── db/migration/      # Flyway (chỉ auth)
    └── test/java/...
```

Package gốc theo service: `com.blueride.auth`, `com.blueride.core`, `com.blueride.gateway`, `com.blueride.crypto`.

---

## 3. Vai trò từng module

### 3.1. `gateway-service`

- Endpoint công khai: `POST /api`.
- Đọc header `X-API-ACTION` → tra file `src/main/resources/actions.yml`.
- Gọi upstream bằng `RestClient`, gắn header `X-Internal-Key`.
- Bọc response thành envelope thống nhất (`success`, `action`, `data`, `error`).
- **Không** chứa business logic (không tính giá, không lưu chuyến, không đăng nhập).

Class chính:

| Class | Việc làm |
|---|---|
| `ApiGatewayController` | Nhận `POST /api`, xử lý thiếu/sai action |
| `ActionCatalog` | Load `actions.yml` |
| `UpstreamClient` | Forward tới auth/core |
| `Envelope` / `ApiError` | Contract JSON trả client |

### 3.2. `auth-service`

- Sở hữu identity: user, credential, hồ sơ tài xế/xe (master data cần ACID).
- Postgres + Flyway (`ddl-auto: validate` — schema chỉ đổi qua migration).
- Nhúng `crypto` để hash mật khẩu (Argon2id) và ký JWT (RSA) khi làm auth.
- Mọi `/internal/**` bắt buộc header `X-Internal-Key`.

### 3.3. `core-service`

- Nghiệp vụ vận hành: trip, matching, giao hàng, GPS.
- MongoDB (document chuyến), Redis (GEO vị trí — reserved), Kafka (event).
- Có thể gọi auth qua internal REST (`AUTH_BASE_URL`) khi cần profile/xe.
- **Không** hash password / ký JWT.

### 3.4. `crypto` (library)

- `PasswordHasher` / `Argon2idPasswordHasher`
- `JwtSigner` / `JwtVerifier` / `RsaJwtService` / `RsaKeyPairs`
- Nếu `JWT_*_PEM` trống → sinh key ephemeral (chỉ phù hợp local/dev).

---

## 4. Luồng request (API công khai)

### Request

```http
POST /api HTTP/1.1
Host: localhost
Content-Type: application/json
X-API-ACTION: auth.ping

{}
```

### Success

```json
{
  "success": true,
  "action": "auth.ping",
  "data": { "service": "auth", "status": "ok" },
  "error": null
}
```

### Error (ví dụ thiếu action)

```json
{
  "success": false,
  "action": "",
  "data": null,
  "error": {
    "code": "MISSING_ACTION",
    "message": "Missing API action"
  }
}
```

| Mã lỗi | Khi nào |
|---|---|
| `MISSING_ACTION` | Không gửi `X-API-ACTION` |
| `UNKNOWN_ACTION` | Action không có trong `actions.yml` |
| `UPSTREAM_ERROR` | Auth/core lỗi hoặc không liên lạc được |
| `UNAUTHORIZED` | Gọi `/internal/**` sai/thiếu `X-Internal-Key` |

### Action hiện có (base scaffold)

| `X-API-ACTION` | Upstream | Path nội bộ |
|---|---|---|
| `auth.ping` | auth | `GET /internal/v1/ping` |
| `core.ping` | core | `GET /internal/v1/ping` |

Health check Docker (không cần internal key): `GET /health` trên từng service.

---

## 5. Quy ước viết code trong từng service

1. **Layer package rõ**: `config`, `health`, `internal`, sau này thêm `domain` / `api` / `persistence` theo use case.
2. **API nội bộ** luôn dưới `/internal/v1/...` và đi qua `InternalApiKeyFilter`.
3. **Không** expose port auth/core ra ngoài qua Nginx — chỉ `/api` → gateway.
4. Schema Postgres: chỉ Flyway (`V2__...sql`, `V3__...`). Không dùng `ddl-auto: update`.
5. Thêm use case mới = thêm handler trong service sở hữu + một dòng trong `gateway-service/.../actions.yml`.
6. Secret (DB password, JWT PEM, `INTERNAL_API_KEY`) không commit; dùng `.env` / GitHub Secrets.

---

## 6. Yêu cầu môi trường

| Công cụ | Phiên bản | Ghi chú |
|---|---|---|
| JDK | **21** (Temurin khuyến nghị) | Gradle toolchain cũng yêu cầu 21 |
| Docker + Compose | Bản mới hỗ trợ `docker compose` | Cần để chạy stack / CI Docker |
| Git | bất kỳ | Clone & deploy VPS |
| curl | bất kỳ | Smoke test API |

Trên Windows: dùng `gradlew.bat`. Trên Linux/macOS: `./gradlew`.

Đặt `JAVA_HOME` trỏ JDK 21 nếu máy còn JDK cũ trên `PATH`.

---

## 7. Build & chạy test

Mọi lệnh Gradle chạy từ thư mục **`backend/`**.

### Build toàn bộ

```bash
cd backend
./gradlew build
```

Windows:

```bat
cd backend
gradlew.bat build
```

### Chỉ một module

```bash
./gradlew :crypto:check
./gradlew :auth-service:check
./gradlew :core-service:check
./gradlew :gateway-service:check
```

`check` = Checkstyle + test.

### Đóng JAR chạy được (bootJar)

```bash
./gradlew :auth-service:bootJar
./gradlew :core-service:bootJar
./gradlew :gateway-service:bootJar
```

File nằm tại:

- `auth-service/build/libs/*.jar`
- `core-service/build/libs/*.jar`
- `gateway-service/build/libs/*.jar`

### Ghi chú test auth

- `AuthWebTests`: luôn chạy (MockMvc, không cần Docker).
- `AuthSchemaTests`: cần Docker (Testcontainers Postgres). Nếu Docker tắt, test này được bỏ qua (`disabledWithoutDocker = true`).

---

## 8. Chạy local bằng Docker Compose

Đây là cách **khuyến nghị** để chạy đủ stack (nginx + 3 service + Postgres + Mongo + Redis + Kafka).

### Bước 1 — Chuẩn bị `.env`

```bash
cd infra
cp .env.example .env
```

Chỉnh `INTERNAL_API_KEY`, mật khẩu DB nếu cần. **Không commit** file `.env`.

### Bước 2 — Build & lên container

```bash
cd infra/docker
docker compose --env-file ../.env up -d --build
```

Lần đầu sẽ build 3 image Java (có thể mất vài phút).

### Bước 3 — Kiểm tra

```bash
docker compose -f infra/docker/compose.yml ps
```

Smoke test:

```bash
curl -s -X POST http://localhost/api \
  -H "X-API-ACTION: auth.ping" \
  -H "Content-Type: application/json"

curl -s -X POST http://localhost/api \
  -H "X-API-ACTION: core.ping" \
  -H "Content-Type: application/json"
```

Kỳ vọng `"success": true`.

### Dừng / xem log

```bash
docker compose -f infra/docker/compose.yml logs -f gateway-service
docker compose -f infra/docker/compose.yml down
```

Xóa luôn volume (mất data local):

```bash
docker compose -f infra/docker/compose.yml down -v
```

### Port lộ ra máy host

| Port | Thành phần | Ghi chú |
|---|---|---|
| `80` | Nginx → gateway | API công khai |
| `5432` | Postgres | Chỉ `127.0.0.1` |
| `27017` | Mongo | Chỉ `127.0.0.1` |
| `6379` | Redis | Chỉ `127.0.0.1` |

Ba service Java **không** publish port ra host trong Compose (chỉ `expose` nội bộ Docker network). Debug trực tiếp container hoặc tạm thêm `ports` nếu cần.

---

## 9. Biến môi trường

Tham chiếu [`infra/.env.example`](../infra/.env.example).

| Biến | Dùng bởi | Ý nghĩa |
|---|---|---|
| `POSTGRES_URL` | auth | JDBC URL |
| `POSTGRES_USERNAME` / `POSTGRES_PASSWORD` | auth, postgres | Credential DB |
| `INTERNAL_API_KEY` | cả 3 service | Shared secret cho `/internal/**` |
| `AUTH_BASE_URL` | gateway, core | URL nội bộ auth |
| `CORE_BASE_URL` | gateway | URL nội bộ core |
| `MONGO_URI` / `MONGO_DB` | core | Mongo |
| `REDIS_ADDR` | core | host:port Redis |
| `KAFKA_BOOTSTRAP_SERVERS` | core | Broker Kafka |
| `JWT_PRIVATE_KEY_PEM` / `JWT_PUBLIC_KEY_PEM` | auth | RSA PEM (để trống = ephemeral local) |

Trong Docker network, hostname service = tên trong Compose (`auth-service`, `core-service`, `postgres`, …).

---

## 10. Cách thêm API mới (action)

Ví dụ thêm `auth.register` (minh họa quy trình, chưa implement nghiệp vụ):

1. **Handler trong auth** — ví dụ `POST /internal/v1/auth/register` (vẫn bảo vệ bằng `X-Internal-Key`).
2. **Đăng ký trong gateway** — sửa `backend/gateway-service/src/main/resources/actions.yml`:

```yaml
actions:
  auth.ping:
    method: GET
    upstream: auth
    path: /internal/v1/ping
  auth.register:
    method: POST
    upstream: auth
    path: /internal/v1/auth/register
  core.ping:
    method: GET
    upstream: core
    path: /internal/v1/ping
```

3. Client gọi:

```bash
curl -s -X POST http://localhost/api \
  -H "X-API-ACTION: auth.register" \
  -H "Content-Type: application/json" \
  -d '{"phone":"...","password":"..."}'
```

4. Viết test ở service sở hữu + (nếu cần) MockWebServer ở gateway.

**Phân ranh nhanh:** dữ liệu user/xe/giấy tờ → `auth-service`. Chuyến đi / matching / GPS / đơn giao → `core-service`. Gateway chỉ thêm dòng catalog, không viết business.

---

## 11. Triển khai (Deploy)

### 11.1. Mô hình hiện tại

- Nhánh `develop` → staging (VPS), workflow [`.github/workflows/deploy.yml`](../.github/workflows/deploy.yml).
- Nhánh `main` → production (**đang tắt** placeholder).
- Trên VPS: clone repo, `docker compose -f infra/docker/compose.yml up -d --build`.

### 11.2. Chuẩn bị VPS (một lần)

1. Cài Docker Engine + Compose plugin, Git, mở port `80` (và `443` nếu sau này gắn TLS).
2. Clone repo vào `~/blue-rider` (hoặc path team thống nhất).
3. Tạo file `.env` trên VPS (không commit), tối thiểu Postgres + `INTERNAL_API_KEY` mạnh.
4. Cấu hình GitHub Environment `staging` với secrets:

| Secret | Mục đích |
|---|---|
| `VPS_HOST` | IP / hostname VPS |
| `VPS_USER` | User SSH |
| `VPS_SSH_PRIVATE_KEY` | Private key deploy |
| `POSTGRES_URL` | JDBC trong mạng Docker |
| `POSTGRES_USERNAME` / `POSTGRES_PASSWORD` | DB |

### 11.3. Deploy thủ công trên VPS

```bash
cd ~/blue-rider
git fetch origin develop
git reset --hard origin/develop

# Đảm bảo .env đã có (không ghi đè bằng git)
docker compose -f infra/docker/compose.yml up -d --build
docker compose -f infra/docker/compose.yml ps
```

Smoke test từ máy local (trỏ domain/IP VPS):

```bash
curl -s -X POST http://<VPS_HOST>/api \
  -H "X-API-ACTION: auth.ping" \
  -H "Content-Type: application/json"
```

### 11.4. Deploy tự động (CI)

Push / merge vào `develop` (thay đổi `backend/**` hoặc `infra/**`) → job Staging:

1. SSH vào VPS.
2. Ghi một phần `.env` (Postgres) từ secrets.
3. `git reset --hard origin/develop`.
4. `docker compose ... up -d --build`.

Production trên `main` cần bật lại job tương tự khi sẵn sàng (TLS, backup DB, secret riêng).

### 11.5. Dockerfile từng service

Mỗi service có Dockerfile multi-stage:

1. Stage build: Temurin JDK 21 + `./gradlew :<service>:bootJar`.
2. Stage runtime: Temurin JRE 21 + `curl` (healthcheck).

Build context luôn là **`backend/`** (để copy `crypto`, Gradle root, checkstyle). Ví dụ trong Compose:

```yaml
auth-service:
  build:
    context: ../../backend
    dockerfile: auth-service/Dockerfile
```

---

## 12. CI/CD

| Workflow | Khi chạy | Việc làm |
|---|---|---|
| `auth-service-ci.yml` | Đổi `auth-service` / `crypto` | `:crypto:test` + `:auth-service:check` |
| `core-service-ci.yml` | Đổi `core-service` | `:core-service:check` |
| `gateway-service-ci.yml` | Đổi `gateway-service` | `:gateway-service:check` |
| `docker-ci.yml` | Đổi `backend` / `infra` | `docker compose config` + build image |
| `deploy.yml` | Push `develop` / `main` | Deploy staging; production đang tắt |

Thông báo Telegram (nếu repo có `TELEGRAM_BOT_TOKEN` / `TELEGRAM_CHAT_ID`).

Nhánh làm việc: `feat/*`, `fix/*`, `chore/*` → PR vào `develop` (squash merge). Chi tiết: [`COMMIT_CONVENTION.md`](COMMIT_CONVENTION.md).

---

## 13. Checklist & xử lý lỗi thường gặp

### Checklist onboarding

- [ ] Cài JDK 21, Docker Desktop/Engine.
- [ ] Clone repo, đọc `docs/PROJECT_CONTEXT.md` + file này.
- [ ] `cd backend && ./gradlew :gateway-service:check` chạy OK.
- [ ] `cp infra/.env.example infra/.env` rồi `docker compose up -d --build`.
- [ ] `curl` `auth.ping` và `core.ping` trả `success: true`.

### Lỗi thường gặp

| Hiện tượng | Nguyên nhân thường gặp | Cách xử lý |
|---|---|---|
| Gradle báo Java version sai | `PATH` trỏ JDK 8/11 | Set `JAVA_HOME` = JDK 21 |
| Auth test schema fail / skip | Docker chưa bật | Bật Docker; Web tests vẫn chạy |
| `UPSTREAM_ERROR` khi curl | Auth/core chưa healthy | `docker compose ps` / xem logs |
| `UNAUTHORIZED` khi gọi internal | Sai `INTERNAL_API_KEY` | Đồng bộ key trên cả 3 service |
| Build Docker chậm / fail cache | Context thiếu `gradlew` | Build từ `infra/docker`, context `../../backend` |
| Flyway / JPA validate fail | Schema lệch code | Chỉ sửa bằng migration mới, không sửa tay DB tùy tiện trên staging |

### Liên hệ tài liệu khác

- Kiến trúc ngắn (EN): [`PROJECT_CONTEXT.md`](PROJECT_CONTEXT.md)
- Commit: [`COMMIT_CONVENTION.md`](COMMIT_CONVENTION.md)
- Quy tắc contributor: [`../AGENTS.md`](../AGENTS.md)

---

*Tài liệu phản ánh base scaffold hiện tại (ping/health/routing). Use case đăng nhập, trip, GPS sẽ bổ sung theo cùng hợp đồng `X-API-ACTION` và ranh giới auth/core ở trên.*
