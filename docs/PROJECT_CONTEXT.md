# PROJECT_CONTEXT — blue-ride backend

Source of truth for backend architecture. Services do not share databases.
Vietnamese onboarding (build, structure, deploy): see [BACKEND_GUIDE.md](BACKEND_GUIDE.md).
Synchronous calls use internal REST on the private Docker network.
Asynchronous work uses Kafka. Clients never call auth or core directly.

## Processes

Three running services. `backend/crypto` is a **library** (Argon2id, JWT-RSA), not a process.

| Process | Stack | Owns | Must not |
|---|---|---|---|
| `gateway-service` | Java 21, Spring Boot | Public `POST /api`, action routing, envelope | Business logic, databases |
| `auth-service` | Java 21, Spring Boot, PostgreSQL | Users, auth, driver/vehicle master data (ACID) | Trip matching, driver GPS |
| `core-service` | Java 21, Spring Boot, MongoDB, Redis, Kafka | Trip, matching, delivery, location | User passwords, JWT signing |

Nginx terminates HTTP and reverse-proxies `/api` to the gateway only.

Vehicle papers/registration live in auth. Trips, matching, GPS, and delivery orders live in core. Core calls auth over internal REST when it needs a profile or vehicle.

## Public contract

- Single public endpoint: `POST /api`
- Routing header: `X-API-ACTION`
- Gateway looks up the action, optionally authenticates (JWT later), forwards to an internal URL, wraps the response.

Envelope:

```json
{
  "success": true,
  "action": "auth.ping",
  "data": {},
  "error": null
}
```

Error shape:

```json
{
  "success": false,
  "action": "unknown.action",
  "data": null,
  "error": { "code": "UNKNOWN_ACTION", "message": "Unknown API action" }
}
```

## Action catalog (base)

Only ping actions exist. Use cases add rows to `gateway-service` `actions.yml` and handlers in the owning service.

| X-API-ACTION | Upstream | Auth |
|---|---|---|
| `auth.ping` | `GET {auth}/internal/v1/ping` | no |
| `core.ping` | `GET {core}/internal/v1/ping` | no |

Unknown or missing `X-API-ACTION` → HTTP 400, envelope `UNKNOWN_ACTION` / `MISSING_ACTION`.

## Internal network

- Header `X-Internal-Key` required on `/internal/**`.
- Health: `GET /health` on each service (no internal key) for Docker checks.
- Gateway injects `X-Internal-Key` when forwarding. Later it will also inject `X-User-Id` / `X-User-Role` from JWT.
- Crypto (JWT-RSA, Argon2id) lives in `backend/crypto`. Auth signs tokens later; core verifies via public key/JWKS. Do not copy hashing/signing into core.

## Data stores (roles)

- **PostgreSQL** (`auth-service`): ACID identity data. Flyway owns schema. No cross-service queries.
- **MongoDB** (`core-service`): trip documents and related operational data (collections added with use cases).
- **Redis** (`core-service`): high-frequency driver location (`GEO`), presence, short-lived locks. Key namespace reserved: `drivers:geo:{vehicleType}`, `trip:offer:{tripId}`. Not used in the base scaffold.
- **Kafka**: event bus between services. Topic names reserved (no business producers yet):
  - `user.events`
  - `trip.events`

## Local stack

`infra/docker/compose.yml` runs nginx, gateway, auth, core, Postgres, MongoDB, Redis, Kafka (KRaft, single broker).

Proof the base works:

```bash
curl -s -X POST http://localhost/api \
  -H "X-API-ACTION: auth.ping" \
  -H "Content-Type: application/json"

curl -s -X POST http://localhost/api \
  -H "X-API-ACTION: core.ping" \
  -H "Content-Type: application/json"
```
