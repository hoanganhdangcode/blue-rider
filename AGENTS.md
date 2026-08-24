# AGENTS.md — blue-rider

Grab-like ride-sharing platform. Backend gồm 2 service tách bạch:

| Service | Stack | Trách nhiệm |
|---|---|---|
| `backend/core-service` | Java 21, Spring Boot, PostgreSQL, JPA, JWT/RSA | Identity, user/driver/vehicle, dữ liệu cần ACID |
| Operator Service (chưa có) | Golang, MongoDB, Redis, Kafka | Trip/matching/location, workload high-throughput |

## Quy tắc bắt buộc

1. **Đọc `docs/PROJECT_CONTEXT.md` trước khi thiết kế/thay đổi kiến trúc** — đây là nguồn chân lý về kiến trúc.
2. Không truy cập chéo database giữa các service; giao tiếp synchronous qua internal REST API (private network), asynchronous qua Kafka.
3. Client chỉ thấy **một** public endpoint (`POST /api`), route bằng header `X-API-ACTION`. Gateway không chứa business logic.
4. Crypto (JWT-RSA, request signing, ECDH, AES-256-GCM, Argon2id) phải nằm trong module riêng, không nhân bản vào business service. Không log/commit secrets.
5. Driver location là high-frequency data → Redis GEO, không query PostgreSQL từng update.
6. Application stateless, thiết kế cho horizontal scaling (Kafka consumer groups, replicas).
7. Mỗi service có Dockerfile riêng; deploy VPS bằng Docker Compose; CI/CD GitHub Actions + Telegram notify.

## Workflow Git

- Nhánh dài hạn: `develop` (staging/test) → `main` (production). Feature branch `feat/*`, `fix/*`, `chore/*` → PR vào `develop`, squash merge, xoá nhánh sau merge.
- **Commit message bằng tiếng Anh**, theo chuẩn Conventional Commits — xem `docs/COMMIT_CONVENTION.md`. Format: `type(scope): short summary`.
