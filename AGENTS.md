# AGENTS.md — blue-ride

Grab-like ride-sharing platform. Backend comprises three running services plus a shared crypto library:

| Module | Stack | Trách nhiệm |
|---|---|---|
| `backend/gateway-service` | Java 21, Spring Boot | Public `POST /api`, route `X-API-ACTION`, envelope |
| `backend/auth-service` | Java 21, Spring Boot, PostgreSQL, JPA | User, auth, driver/vehicle master data (ACID) |
| `backend/core-service` | Java 21, Spring Boot, MongoDB, Redis, Kafka | Trip, matching, giao hàng, location |
| `backend/crypto` | Java library (not a process) | Argon2id, JWT-RSA — used by auth, never copied into core |

## Quy tắc bắt buộc

1. **Đọc `docs/PROJECT_CONTEXT.md` trước khi thiết kế/thay đổi kiến trúc** — đây là nguồn chân lý về kiến trúc. Hướng dẫn onboarding (build, cấu trúc code, deploy) bằng tiếng Việt: `docs/BACKEND_GUIDE.md`.
2. Không truy cập chéo database giữa các service; giao tiếp synchronous qua internal REST API (private network), asynchronous qua Kafka.
3. Client chỉ thấy **một** public endpoint (`POST /api`), route bằng header `X-API-ACTION`. Gateway không chứa business logic.
4. Crypto (JWT-RSA, request signing, ECDH, AES-256-GCM, Argon2id) phải nằm trong module `backend/crypto`, không nhân bản vào business service. Không log/commit secrets.
5. Driver location là high-frequency data → Redis GEO, không query PostgreSQL từng update.
6. Application stateless, thiết kế cho horizontal scaling (Kafka consumer groups, replicas).
7. Mỗi service có Dockerfile riêng; deploy VPS bằng Docker Compose; CI/CD GitHub Actions + Telegram notify.

## Workflow Git

- Nhánh dài hạn: `develop` (staging/test) → `main` (production). Feature branch `feat/*`, `fix/*`, `chore/*` → PR vào `develop`, squash merge, xoá nhánh sau merge.
- **Commit message bằng tiếng Anh**, theo chuẩn Conventional Commits — xem `docs/COMMIT_CONVENTION.md`. Format: `type(scope): short summary`.
