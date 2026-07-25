# Getting started

<p align="center">
  <a href="getting-started.md"><img src="https://img.shields.io/badge/EN--US-ACTIVE-0052CC?style=for-the-badge&logo=googletranslate&logoColor=white" alt="EN-US active" /></a>
  <a href="../pt-BR/primeiros-passos.md"><img src="https://img.shields.io/badge/PT--BR-Switch-2E7D32?style=for-the-badge&logo=googletranslate&logoColor=white" alt="Switch to PT-BR" /></a>
</p>

<p align="center">
  <a href="../../README.md"><img src="https://img.shields.io/badge/🏠_Home-0B1F3A?style=for-the-badge" alt="Home" /></a>
  <a href="product.md"><img src="https://img.shields.io/badge/📦_Product-0B1F3A?style=for-the-badge" alt="Product" /></a>
  <a href="stack.md"><img src="https://img.shields.io/badge/🧰_Stack-0B1F3A?style=for-the-badge" alt="Stack" /></a>
  <a href="architecture.md"><img src="https://img.shields.io/badge/🏗️_Architecture-0B1F3A?style=for-the-badge" alt="Architecture" /></a>
  <a href="roadmap.md"><img src="https://img.shields.io/badge/🗺️_Roadmap-0B1F3A?style=for-the-badge" alt="Roadmap" /></a>
  <a href="getting-started.md"><img src="https://img.shields.io/badge/🚀_Getting%20Started-ACTIVE-1565C0?style=for-the-badge" alt="Getting Started" /></a>
</p>

---

## Prerequisites

<p align="center">
  <img src="https://img.shields.io/badge/Java-25%20Corretto-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java" />
  <img src="https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker" />
  <img src="https://img.shields.io/badge/Gradle-Wrapper-02303A?style=for-the-badge&logo=gradle&logoColor=white" alt="Gradle" />
</p>

- Java 25 (Amazon Corretto recommended)
- Docker + Docker Compose
- Repository clone with Gradle Wrapper

## 1. Start infrastructure

```bash
docker compose -f deploy/docker-compose.yml up -d
```

Starts **PostgreSQL** and **Redis**.

## 2. Run the planes

```bash
./gradlew :auth-control-plane:bootRun
./gradlew :auth-data-plane:bootRun
```

| Service | URL |
|---|---|
| Control plane | `http://localhost:8081` |
| Data plane | `http://localhost:8080` |

## 3. Onboard a tenant

```bash
curl -u platform-admin:change-me -X POST http://localhost:8081/platform/v1/tenants \
  -H 'Content-Type: application/json' \
  -d '{"slug":"acme","displayName":"Acme Corp","adminUsername":"admin","adminPassword":"ChangeMeNow1!"}'
```

Creates schema `t_acme`, runs tenant migrations, seeds the admin user.

## 4. Login (password)

```bash
curl -X POST http://localhost:8080/t/acme/v1/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"admin","password":"ChangeMeNow1!"}'
```

Response includes `accessToken`, `refreshToken`, and TTLs.

## 5. Refresh / logout

```bash
# Refresh (rotates the refresh token)
curl -X POST http://localhost:8080/t/acme/v1/auth/token/refresh \
  -H 'Content-Type: application/json' \
  -d '{"refreshToken":"<token>"}'

# Logout (revokes refresh)
curl -X POST http://localhost:8080/t/acme/v1/auth/logout \
  -H 'Content-Type: application/json' \
  -d '{"refreshToken":"<token>"}'
```

## Useful endpoints

| Method | Path | Plane |
|---|---|---|
| `POST` | `/platform/v1/tenants` | Control |
| `POST` | `/t/{slug}/v1/auth/login` | Data |
| `POST` | `/t/{slug}/v1/auth/api-key` | Data |
| `POST` | `/t/{slug}/v1/auth/token/refresh` | Data |
| `POST` | `/t/{slug}/v1/auth/logout` | Data |
| `GET` | `/t/{slug}/v1/.well-known/jwks.json` | Data |
| `GET` | `/t/{slug}/v1/auth/oauth2/{provider}/authorize` | Data (501 stub) |
| `POST` | `/t/{slug}/v1/auth/mfa/totp/verify` | Data (501 stub) |

## Next reads

- [Product](product.md)
- [Architecture](architecture.md)
- [Roadmap](roadmap.md)

<p align="center">
  <a href="getting-started.md"><img src="https://img.shields.io/badge/EN--US-ACTIVE-0052CC?style=for-the-badge&logo=googletranslate&logoColor=white" alt="EN-US active" /></a>
  <a href="../pt-BR/primeiros-passos.md"><img src="https://img.shields.io/badge/PT--BR-Switch-2E7D32?style=for-the-badge&logo=googletranslate&logoColor=white" alt="Switch to PT-BR" /></a>
</p>
