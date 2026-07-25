# Product

<p align="center">
  <a href="product.md"><img src="https://img.shields.io/badge/EN--US-ACTIVE-0052CC?style=for-the-badge&logo=googletranslate&logoColor=white" alt="EN-US active" /></a>
  <a href="../pt-BR/produto.md"><img src="https://img.shields.io/badge/PT--BR-Switch-2E7D32?style=for-the-badge&logo=googletranslate&logoColor=white" alt="Switch to PT-BR" /></a>
</p>

<p align="center">
  <a href="../../README.md"><img src="https://img.shields.io/badge/🏠_Home-0B1F3A?style=for-the-badge" alt="Home" /></a>
  <a href="product.md"><img src="https://img.shields.io/badge/📦_Product-ACTIVE-1565C0?style=for-the-badge" alt="Product" /></a>
  <a href="stack.md"><img src="https://img.shields.io/badge/🧰_Stack-0B1F3A?style=for-the-badge" alt="Stack" /></a>
  <a href="architecture.md"><img src="https://img.shields.io/badge/🏗️_Architecture-0B1F3A?style=for-the-badge" alt="Architecture" /></a>
  <a href="roadmap.md"><img src="https://img.shields.io/badge/🗺️_Roadmap-0B1F3A?style=for-the-badge" alt="Roadmap" /></a>
  <a href="getting-started.md"><img src="https://img.shields.io/badge/🚀_Getting%20Started-0B1F3A?style=for-the-badge" alt="Getting Started" /></a>
</p>

---

## What is Auth SaaS?

Auth SaaS is a **commercial multi-tenant authentication platform** designed to be embedded as the identity backbone of B2B products.

Instead of shipping a single-tenant JWT demo, the platform treats every customer organization as an isolated tenant with its own schema, admin user, and authentication provider configuration.

<p align="center">
  <img src="https://img.shields.io/badge/🎯_Multi--tenant-0B1F3A?style=for-the-badge" alt="Multi-tenant" />
  <img src="https://img.shields.io/badge/🔐_Auth%20API-0B1F3A?style=for-the-badge" alt="Auth API" />
  <img src="https://img.shields.io/badge/🏢_B2B%20SaaS-0B1F3A?style=for-the-badge" alt="B2B SaaS" />
  <img src="https://img.shields.io/badge/⚡_Two%20planes-0B1F3A?style=for-the-badge" alt="Two planes" />
</p>

## Who it is for

| Audience | Why it fits |
|---|---|
| SaaS founders / platform teams | Need tenant-aware auth without building IdP from scratch |
| Backend engineers | Clear module boundaries, Spring Boot deployables, schema isolation |
| Security-minded teams | Argon2id, rotating refresh tokens, separable admin vs auth traffic |

## Value proposition

1. **Tenant isolation by design** — each tenant gets `t_<slug>` schema, migrations, admin, and default providers.
2. **Independent scale** — control plane (onboarding) and data plane (login/tokens) are separate processes.
3. **Auth methods as product surface** — password, refresh, API keys today; OIDC and MFA next; passkeys/SAML later.
4. **Ops-friendly stack** — PostgreSQL, Redis, Flyway, Docker Compose, CI on Corretto 25.

## Product surface (API)

| Capability | Plane | Status |
|---|---|---|
| Create / onboard tenant | Control (`:8081`) | Available |
| Password login | Data (`:8080`) | Available |
| Refresh token rotate + revoke | Data | Available |
| API key login | Data | Available |
| JWKS | Data | Available |
| OIDC authorize | Data | Scaffolded (501) |
| MFA TOTP verify | Data | Scaffolded (501) |

## Positioning

```text
Your product  ──►  Auth SaaS Data Plane   (users authenticate here)
Platform ops  ──►  Auth SaaS Control Plane (tenants are provisioned here)
```

Auth SaaS is **not** a full customer-facing UI IdP console yet. It is the **API product**: onboarding + authentication + tokens, ready to sit behind your apps and gateways.

## Related pages

- [Architecture](architecture.md) — how planes and tenancy work
- [Roadmap](roadmap.md) — what ships next
- [Getting started](getting-started.md) — try it locally

<p align="center">
  <a href="product.md"><img src="https://img.shields.io/badge/EN--US-ACTIVE-0052CC?style=for-the-badge&logo=googletranslate&logoColor=white" alt="EN-US active" /></a>
  <a href="../pt-BR/produto.md"><img src="https://img.shields.io/badge/PT--BR-Switch-2E7D32?style=for-the-badge&logo=googletranslate&logoColor=white" alt="Switch to PT-BR" /></a>
</p>
