# Task: Thiết kế và triển khai Backend MVP

Đọc và tuân thủ `AGENTS.md`.

**BẮT BUỘC:** Trước khi thiết kế hoặc thay đổi architecture/code, phải đọc:

```text
docs/PROJECT_CONTEXT.md
```

Đây là nguồn chân lý về architecture hiện tại. Nếu task này có điểm khác với `PROJECT_CONTEXT.md`, ưu tiên `PROJECT_CONTEXT.md`.

---

# 1. Scope MVP

Chỉ tập trung triển khai 3 Spring Boot service:

```text
Client
   │
   ▼
Nginx
   │
   ▼
API Gateway
   │
   ├──────────────► Core Service ─────► PostgreSQL
   │
   └──────────────► Operator Service
```

## API Gateway

* Java 21
* Spring Boot
* Là public entry point.
* Client chỉ gọi:

```http
POST /api
X-API-ACTION: <action>
```

* Route request tới Core Service hoặc Operator Service.
* Không chứa business logic.
* Không truy cập database.

## Core Service

* Java 21
* Spring Boot
* PostgreSQL
* Spring Data JPA
* Spring Security
* JWT/RSA

Phụ trách:

* Account
* User
* Authentication
* Driver
* Vehicle
* Identity-related data
* Dữ liệu cần transaction/ACID.

## Operator Service

* Java 21
* Spring Boot

Phụ trách nghiệp vụ:

* Booking
* Trip
* Ride lifecycle
* Các nghiệp vụ đặt xe/operator sau này.

Operator Service **không truy cập trực tiếp PostgreSQL của Core Service**.

---

# 2. Những thứ KHÔNG triển khai trong MVP

Không triển khai trong task này:

* Kafka
* Redis
* MongoDB
* ECDH
* AES-256-GCM request encryption
* Request signing
* Distributed tracing
* Service mesh
* Vault/KMS
* SMS provider thật
* Complex event-driven architecture
* Advanced horizontal-scaling infrastructure.

Các thành phần trên sẽ được thiết kế/triển khai ở phase sau khi hệ thống cần scale.

**Không được tự ý đưa các thành phần này vào MVP chỉ để "future-proof".**

Hiện tại ưu tiên:

```text
Simple
→ Correct
→ Secure
→ Maintainable
→ Easy to deploy
```

---

# 3. Authentication MVP

Authentication hiện tại sử dụng:

```text
Phone Number + OTP
```

Chưa tích hợp SMS provider thật.

Mock OTP cố định:

```text
456789
```

Flow:

```text
Client
   │
   │ Request OTP
   ▼
API Gateway
   │
   ▼
Core Service
   │
   └── Mock OTP = 456789
```

Sau đó:

```text
Client
   │
   │ Verify OTP
   ▼
API Gateway
   │
   ▼
Core Service
   │
   ├── Validate OTP
   ├── Find account
   │
   ├── Existing account
   │       └── Login
   │
   └── New account
           └── Create account
   │
   ▼
Generate JWT
   │
   ▼
Return JWT
```

---

# 4. Request OTP

Public API:

```http
POST /api
X-API-ACTION: AUTH_REQUEST_OTP
Content-Type: application/json
```

Request:

```json
{
  "phoneNumber": "0912345678"
}
```

Gateway route tới Core Service.

Core Service:

1. Validate phone number.
2. Tạo OTP challenge theo implementation đơn giản phù hợp MVP.
3. OTP mock là `456789`.
4. Không expose OTP trong production response.

Response:

```json
{
  "success": true,
  "message": "OTP sent"
}
```

Mock OTP có thể được cấu hình:

```yaml
auth:
  otp:
    mock:
      enabled: true
      code: ${MOCK_OTP_CODE:456789}
```

Không hard-code OTP trực tiếp trong controller.

---

# 5. Verify OTP

Public API:

```http
POST /api
X-API-ACTION: AUTH_VERIFY_OTP
Content-Type: application/json
```

Request:

```json
{
  "phoneNumber": "0912345678",
  "otp": "456789"
}
```

Core Service:

```text
Validate phone
      ↓
Validate OTP
      ↓
Find account by phone
      ↓
 ┌───────────────┐
 │ Account exists│
 └───────┬───────┘
      YES│        NO
         │         │
         ▼         ▼
       Login     Create
         │         │
         └────┬────┘
              ▼
         Generate JWT
              ▼
          Return JWT
```

Nếu account chưa tồn tại, OTP verification thành công sẽ tự động tạo account.

---

# 6. Account

Nếu database hiện tại chưa có account model phù hợp, thiết kế tối thiểu:

```text
account
---------
id
phone_number
status
created_at
updated_at
```

Yêu cầu:

```text
phone_number UNIQUE
```

Không tạo duplicate account khi có concurrent login request.

Sử dụng transaction + database unique constraint.

Không cần password authentication trong MVP.

---

# 7. JWT

JWT sử dụng RSA.

Core Service là service chịu trách nhiệm identity và phát hành JWT.

JWT payload tối thiểu:

```json
{
  "sub": "<account-id>",
  "phone": "<phone-number>",
  "iat": 1234567890,
  "exp": 1234567890
}
```

Không đưa dữ liệu nhạy cảm không cần thiết vào JWT.

Response:

```json
{
  "accessToken": "...",
  "tokenType": "Bearer",
  "expiresIn": 3600,
  "account": {
    "id": "...",
    "phoneNumber": "0912345678"
  }
}
```

---

# 8. RSA Key Management

Core Service khi startup phải kiểm tra:

```text
/app/keys/private.key
/app/keys/public.key
```

Flow:

```text
Spring Boot startup
       ↓
Check private.key
Check public.key
       ↓
Both exist?
   │          │
  YES        NO
   │          │
   ▼          ▼
 Load       Generate
 keys       RSA pair
              ↓
          Save keys
              ↓
           Load keys
```

Nếu key đã tồn tại hợp lệ:

**Không generate lại.**

Nếu một trong hai key bị thiếu:

**Generate một key pair mới hoàn chỉnh.**

Nếu key bị corrupted hoặc private/public không cùng pair:

**Fail startup**, không tự động overwrite key đang tồn tại.

RSA key generation nằm trong crypto/security component riêng, không rải logic generate key trong business service.

---

# 9. Docker Key Persistence

Không commit private key vào Git.

Không `COPY` private key vào Docker image.

Docker Compose mount thư mục key:

```yaml
services:
  core-service:
    build: ./backend/core-service
    volumes:
      - ./secrets/core:/app/keys
    environment:
      JWT_KEYS_DIRECTORY: /app/keys
```

Thêm secrets vào `.gitignore`:

```text
secrets/
*.key
```

Mục tiêu:

```text
Host
└── secrets/core
    ├── private.key
    └── public.key
          │
          │ Docker volume
          ▼
Container
└── /app/keys
    ├── private.key
    └── public.key
```

Restart/recreate container không được làm mất key.

---

# 10. Internal communication

Gateway gọi Core Service và Operator Service bằng internal REST.

Ví dụ:

```text
http://core-service:8080/internal/...
http://operator-service:8080/internal/...
```

Sử dụng Docker private network.

Không expose Core Service và Operator Service trực tiếp ra internet nếu không cần.

MVP chưa cần Kafka hay message broker.

---

# 11. JWT Authentication

Các API protected sau này phải yêu cầu:

```http
Authorization: Bearer <JWT>
```

Gateway có thể validate JWT trước khi route request nếu phù hợp với architecture trong `PROJECT_CONTEXT.md`.

Core Service vẫn phải bảo vệ các internal endpoint cần authentication.

Không coi `/internal/**` là tự động trusted chỉ vì nằm trong Docker network.

Tuy nhiên, không xây dựng service-to-service security phức tạp trong MVP nếu chưa cần.

---

# 12. API Actions

Tối thiểu:

```text
AUTH_REQUEST_OTP
AUTH_VERIFY_OTP
```

Thiết kế action handling tập trung, không hard-code string rải rác trong controller.

Sau này có thể mở rộng:

```text
AUTH_REFRESH_TOKEN
AUTH_LOGOUT
ACCOUNT_GET_PROFILE
DRIVER_GET_PROFILE
BOOKING_CREATE
TRIP_GET
```

Nhưng **không cần implement các action này trong task nếu chưa có requirement.**

---

# 13. Service boundaries

Bắt buộc giữ boundary:

```text
Gateway
    ↓
Routing only

Core Service
    ↓
Identity / Account / User / Driver / Vehicle

Operator Service
    ↓
Booking / Trip / Ride
```

Không:

```text
Operator Service → Core PostgreSQL
Gateway → PostgreSQL
Gateway → Business logic
```

---

# 14. Project structure

Trước khi tạo file:

1. Đọc `docs/PROJECT_CONTEXT.md`.
2. Kiểm tra repository structure hiện tại.
3. Kiểm tra Core Service hiện tại.
4. Kiểm tra entity/schema PostgreSQL hiện tại.
5. Kiểm tra Docker Compose hiện tại.
6. Reuse implementation hiện có.
7. Chỉ tạo những module/class thực sự cần thiết.

Không tự ý rewrite project hiện tại.

---

# 15. Testing

Implement test tối thiểu cho:

### OTP

* Valid phone.
* Invalid phone.
* Correct OTP.
* Wrong OTP.
* Mock OTP disabled.
* Existing account.
* New account.

### Account

* Phone unique.
* Concurrent account creation không tạo duplicate.

### JWT

* Generate token.
* Verify valid token.
* Reject invalid signature.
* Reject expired token.

### RSA keys

* Không có key → generate.
* Có key → load, không generate lại.
* Restart → key không thay đổi.
* Missing một key → xử lý bằng cách generate lại pair theo policy.
* Corrupted key → fail startup.
* Mismatched key pair → fail startup.

---

# 16. Documentation

Sau khi implementation xong, cập nhật documentation liên quan:

* Authentication flow.
* Service responsibilities.
* Public API.
* Internal API.
* JWT flow.
* RSA key management.
* Docker deployment.

Không thêm Kafka/Redis/ECDH/AES/etc. vào MVP documentation như dependency bắt buộc.

Nếu có section "Future scaling", chỉ ghi chú ngắn rằng các thành phần scale nâng cao sẽ được triển khai ở phase sau.

---

# 17. Expected result

Sau khi hoàn thành, hệ thống MVP phải chạy được:

```text
Android
   │
   │ POST /api
   │ X-API-ACTION: AUTH_REQUEST_OTP
   ▼
Nginx
   ▼
API Gateway
   ▼
Core Service
   ▼
PostgreSQL
```

và:

```text
Android
   │
   │ POST /api
   │ X-API-ACTION: AUTH_VERIFY_OTP
   │ OTP = 456789
   ▼
Nginx
   ▼
API Gateway
   ▼
Core Service
   │
   ├── Find/Create Account
   ├── Generate RSA JWT
   ▼
API Gateway
   ▼
Android
```

JWT sau khi đăng nhập thành công có thể được sử dụng để gọi các protected API.

## Nguyên tắc quan trọng

Đây là **MVP architecture**.

Không over-engineer.

Chưa cần:

```text
Kafka
Redis
MongoDB
ECDH
AES request encryption
Request signing
Vault
KMS
Service mesh
```

Chỉ xây nền tảng sạch và đúng boundary để các thành phần trên có thể được bổ sung ở phase scale/security tiếp theo mà không phải rewrite toàn bộ hệ thống.
