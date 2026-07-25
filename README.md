# Auth SaaS

> Language: **EN-US** | [PT-BR](docs/pt-BR/README.md)

Multi-tenant authentication platform (`com.auth.saas`) with separated **control plane** and **data plane**.

## Stack

- Java 25 LTS (Amazon Corretto)
- Spring Boot 4.1.x / Spring Framework 7
- Gradle 9 (Kotlin DSL)
- PostgreSQL 17 (schema-per-tenant)
- Redis 7
- Flyway
- Argon2id password hashing

## Modules

| Module | Description |
|---|---|
| `auth-domain` | Domain contracts |
| `auth-persistence` | Platform/tenant persistence + provisioning |
| `auth-control-plane` | Tenant onboarding API (`:8081`) |
| `auth-data-plane` | Auth/token API (`:8080`) |

## Quick start

```bash
docker compose -f deploy/docker-compose.yml up -d
./gradlew :auth-control-plane:bootRun
./gradlew :auth-data-plane:bootRun
```

Onboard a tenant:

```bash
curl -u platform-admin:change-me -X POST http://localhost:8081/platform/v1/tenants \
  -H 'Content-Type: application/json' \
  -d '{"slug":"acme","displayName":"Acme Corp","adminUsername":"admin","adminPassword":"ChangeMeNow1!"}'
```

Login:

```bash
curl -X POST http://localhost:8080/t/acme/v1/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"admin","password":"ChangeMeNow1!"}'
```

## Docs

- [EN-US docs](docs/en-US/README.md)
- [PT-BR docs](docs/pt-BR/README.md)
- [ADR-001 (EN)](docs/en-US/adr/ADR-001-stack-and-architecture.md)
- [ADR-001 (PT)](docs/pt-BR/adr/ADR-001-stack-and-architecture.md)
