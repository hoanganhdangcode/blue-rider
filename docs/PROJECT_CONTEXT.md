# PROJECT_CONTEXT — blue-ride backend

Source of truth for backend architecture. Services do not share databases.
Synchronous calls use internal REST on the private Docker network.
Asynchronous work uses Kafka. Clients never call core or operator directly.

## Processes

| Process | Stack | Owns | Must not |
|---|---|---|---|
| `gateway-service` | Java 21, Spring Boot | Public `POST /api`, action routing, envelope | Business logic, databases |
| `core-service` | Java 21, Spring Boot, PostgreSQL | Identity and other ACID data (future) | Trip matching, driver GPS |
| `operator-service` | Go, MongoDB, Redis, Kafka | Trip/matching/location (future) | User passwords, JWT signing |

Nginx terminates HTTP and reverse-proxies `/api` to the gateway only.

## Public contract

- Single public endpoint: `POST /api`
- Routing header: `X-API-ACTION`
- Gateway looks up the action, optionally authenticates (JWT later), forwards to an internal URL, wraps the response.

Envelope:

```json
{
  "success": true,
  "action": "core.ping",
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
| `core.ping` | `GET {core}/internal/v1/ping` | no |
| `operator.ping` | `GET {operator}/internal/v1/ping` | no |

Unknown or missing `X-API-ACTION` → HTTP 400, envelope `UNKNOWN_ACTION` / `MISSING_ACTION`.

## Internal network

- Header `X-Internal-Key` required on `/internal/**`.
- Health: `GET /health` on each service (no internal key) for Docker checks.
- Gateway injects `X-Internal-Key` when forwarding. Later it will also inject `X-User-Id` / `X-User-Role` from JWT.
- Crypto (JWT-RSA, Argon2id, ECDH, AES-256-GCM) lives in `backend/crypto`. Core signs tokens later; operator verifies via JWKS. Do not copy hashing/signing into operator.

## Data stores (roles)

- **PostgreSQL** (`core-service`): ACID identity data. Flyway owns schema. No cross-service queries.
- **MongoDB** (`operator-service`): trip documents and related operational data (collections added with use cases).
- **Redis** (`operator-service`): high-frequency driver location (`GEO`), presence, short-lived locks. Key namespace reserved: `drivers:geo:{vehicleType}`, `trip:offer:{tripId}`. Not used in the base scaffold.
- **Kafka**: event bus between services. Topic names reserved (no business producers yet):
  - `user.events`
  - `trip.events`

## Local stack

`infra/docker/compose.yml` runs nginx, gateway, core, operator, Postgres, MongoDB, Redis, Kafka (KRaft, single broker).

Proof the base works:

```bash
curl -s -X POST http://localhost/api \
  -H "X-API-ACTION: core.ping" \
  -H "Content-Type: application/json"

curl -s -X POST http://localhost/api \
  -H "X-API-ACTION: operator.ping" \
  -H "Content-Type: application/json"
```
