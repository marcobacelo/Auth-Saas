# Auth SaaS

<p align="center">
  <img src="https://img.shields.io/badge/Auth%20SaaS-Multi--tenant%20Identity%20Platform-0B1F3A?style=for-the-badge&labelColor=06101C" alt="Auth SaaS" />
</p>

<p align="center">
  <a href="README.md"><img src="https://img.shields.io/badge/EN--US-ACTIVE-0052CC?style=for-the-badge&logo=googletranslate&logoColor=white" alt="EN-US active" /></a>
  <a href="docs/pt-BR/README.md"><img src="https://img.shields.io/badge/PT--BR-Trocar-2E7D32?style=for-the-badge&logo=googletranslate&logoColor=white" alt="Switch to PT-BR" /></a>
</p>

<p align="center">
  <b>Commercial multi-tenant authentication platform</b><br/>
  Onboard tenants. Authenticate users. Issue tokens. Scale control and data planes independently.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-25%20LTS-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 25" />
  <img src="https://img.shields.io/badge/Spring%20Boot-4.1-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Gradle-9-02303A?style=for-the-badge&logo=gradle&logoColor=white" alt="Gradle" />
  <img src="https://img.shields.io/badge/PostgreSQL-17-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL" />
  <img src="https://img.shields.io/badge/Redis-7-DC382D?style=for-the-badge&logo=redis&logoColor=white" alt="Redis" />
  <img src="https://img.shields.io/badge/Flyway-Migrations-CC0200?style=for-the-badge&logo=flyway&logoColor=white" alt="Flyway" />
  <img src="https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker" />
  <img src="https://img.shields.io/badge/Argon2id-OWASP-1B1F23?style=for-the-badge&logo=let'sencrypt&logoColor=white" alt="Argon2id" />
</p>

---

## Navigation

| Page | Description |
|---|---|
| [Product](docs/en-US/product.md) | What Auth SaaS is, who it is for, and the value proposition |
| [Stack](docs/en-US/stack.md) | Technologies, versions, and why each choice was made |
| [Architecture](docs/en-US/architecture.md) | Control plane, data plane, schema-per-tenant, modules |
| [Roadmap](docs/en-US/roadmap.md) | Implemented vs planned — MVP and beyond |
| [Getting started](docs/en-US/getting-started.md) | Local run, onboard a tenant, login |
| [ADR-001](docs/en-US/adr/ADR-001-stack-and-architecture.md) | Architecture decision record |

<p align="center">
  <a href="docs/en-US/product.md"><img src="https://img.shields.io/badge/📦_Product-0B1F3A?style=for-the-badge" alt="Product" /></a>
  <a href="docs/en-US/stack.md"><img src="https://img.shields.io/badge/🧰_Stack-0B1F3A?style=for-the-badge" alt="Stack" /></a>
  <a href="docs/en-US/architecture.md"><img src="https://img.shields.io/badge/🏗️_Architecture-0B1F3A?style=for-the-badge" alt="Architecture" /></a>
  <a href="docs/en-US/roadmap.md"><img src="https://img.shields.io/badge/🗺️_Roadmap-0B1F3A?style=for-the-badge" alt="Roadmap" /></a>
  <a href="docs/en-US/getting-started.md"><img src="https://img.shields.io/badge/🚀_Getting%20Started-0B1F3A?style=for-the-badge" alt="Getting Started" /></a>
</p>

---

## Product in one glance

Auth SaaS (`com.auth.saas`) is an identity backend for B2B SaaS products that need:

- **Tenant isolation** — schema-per-tenant on PostgreSQL
- **Separated planes** — onboarding/admin vs authentication traffic
- **Modern credentials** — Argon2id passwords, rotating refresh tokens, API keys
- **Extensible auth surface** — OIDC and MFA TOTP scaffolded for the next iteration

| Plane | Port | Responsibility |
|---|---|---|
| Control plane | `:8081` | Tenant onboarding and platform admin |
| Data plane | `:8080` | Login, tokens, JWKS, auth methods |

---

## Implementation status

| Capability | Status |
|---|---|
| Multi-module Gradle platform | Done |
| Tenant onboarding API | Done |
| Schema-per-tenant + Flyway | Done |
| Password login (Argon2id) | Done |
| Refresh rotate / revoke (Redis) | Done |
| API key authentication | Done |
| JWKS endpoint | Done |
| OIDC / OAuth2 providers | Next |
| MFA TOTP | Next |
| Passkeys, SAML, SCIM, risk scoring | Later |

Full detail: **[Roadmap](docs/en-US/roadmap.md)**.

---

## Quick start

```bash
docker compose -f deploy/docker-compose.yml up -d
./gradlew :auth-control-plane:bootRun
./gradlew :auth-data-plane:bootRun
```

Onboard a tenant and login — see **[Getting started](docs/en-US/getting-started.md)**.

---

## Modules

| Module | Role |
|---|---|
| `auth-domain` | Domain contracts and models |
| `auth-persistence` | Platform/tenant persistence + provisioning |
| `auth-control-plane` | Tenant onboarding API (`:8081`) |
| `auth-data-plane` | Auth / token API (`:8080`) |

---

## Docs hubs

- [EN-US documentation](docs/en-US/README.md)
- [PT-BR documentation](docs/pt-BR/README.md)

<p align="center">
  <a href="README.md"><img src="https://img.shields.io/badge/EN--US-ACTIVE-0052CC?style=for-the-badge&logo=googletranslate&logoColor=white" alt="EN-US active" /></a>
  <a href="docs/pt-BR/README.md"><img src="https://img.shields.io/badge/PT--BR-Trocar-2E7D32?style=for-the-badge&logo=googletranslate&logoColor=white" alt="Switch to PT-BR" /></a>
</p>
