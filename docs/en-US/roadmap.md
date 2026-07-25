# Roadmap

<p align="center">
  <a href="roadmap.md"><img src="https://img.shields.io/badge/EN--US-ACTIVE-0052CC?style=for-the-badge&logo=googletranslate&logoColor=white" alt="EN-US active" /></a>
  <a href="../pt-BR/roadmap.md"><img src="https://img.shields.io/badge/PT--BR-Switch-2E7D32?style=for-the-badge&logo=googletranslate&logoColor=white" alt="Switch to PT-BR" /></a>
</p>

<p align="center">
  <a href="../../README.md"><img src="https://img.shields.io/badge/🏠_Home-0B1F3A?style=for-the-badge" alt="Home" /></a>
  <a href="product.md"><img src="https://img.shields.io/badge/📦_Product-0B1F3A?style=for-the-badge" alt="Product" /></a>
  <a href="stack.md"><img src="https://img.shields.io/badge/🧰_Stack-0B1F3A?style=for-the-badge" alt="Stack" /></a>
  <a href="architecture.md"><img src="https://img.shields.io/badge/🏗️_Architecture-0B1F3A?style=for-the-badge" alt="Architecture" /></a>
  <a href="roadmap.md"><img src="https://img.shields.io/badge/🗺️_Roadmap-ACTIVE-1565C0?style=for-the-badge" alt="Roadmap" /></a>
  <a href="getting-started.md"><img src="https://img.shields.io/badge/🚀_Getting%20Started-0B1F3A?style=for-the-badge" alt="Getting Started" /></a>
</p>

---

## Where we are

Foundation rewrite is **merged on `main`**: multi-module Auth SaaS with control plane, data plane, schema-per-tenant, and core auth APIs.

<p align="center">
  <img src="https://img.shields.io/badge/Phase-Foundation-2E7D32?style=for-the-badge&logo=checkmarx&logoColor=white" alt="Foundation done" />
  <img src="https://img.shields.io/badge/Next-OIDC%20%2B%20MFA%20TOTP-FB8C00?style=for-the-badge&logo=openid&logoColor=white" alt="Next OIDC MFA" />
  <img src="https://img.shields.io/badge/Later-Passkeys%20·%20SAML%20·%20SCIM-607D8B?style=for-the-badge&logo=yubico&logoColor=white" alt="Later" />
</p>

---

## MVP v1 — authentication checklist

Aligned with [ADR-001](adr/ADR-001-stack-and-architecture.md).

| # | Capability | Status | Notes |
|---|---|---|---|
| 1 | Password (Argon2id) | Done | Login endpoint live |
| 2 | Refresh token (rotating) + revoke | Done | Redis-backed |
| 3 | OIDC / OAuth2 (Google, Microsoft, custom IdP) | Next | `OidcStubController` returns 501 |
| 4 | API keys (machine clients) | Done | API key login live |
| 5 | MFA TOTP | Next | `MfaTotpStubController` returns 501 |

### Platform foundation (also delivered)

| Capability | Status |
|---|---|
| Gradle multi-module under `com.auth.saas` | Done |
| Control plane tenant onboarding | Done |
| Schema-per-tenant + Flyway provisioning | Done |
| JWKS endpoint | Done |
| Docker Compose (Postgres + Redis) | Done |
| CI (Corretto 25) | Done |
| Bilingual docs + ADR-001 | Done |

---

## Next iteration (immediate)

| Item | Goal |
|---|---|
| **OIDC authorization-code flow** | Real `/oauth2/{provider}/authorize` + callback, IdP config per tenant |
| **MFA TOTP** | Enroll + verify using `mfa_totp_secrets`, gate login when enabled |

These replace the scaffolded 501 stubs in the data plane.

---

## Post-MVP roadmap

| Theme | Items |
|---|---|
| Strong auth | Passkeys / WebAuthn |
| Enterprise federation | SAML |
| Alternative MFA | SMS OTP |
| Directory sync | SCIM |
| Adaptive security | Risk scoring |
| Tenancy scale-out | Dedicated database per enterprise tenant |

```mermaid
timeline
  title Auth SaaS delivery
  section Foundation
    Platform rewrite : Done
    Password + Refresh + API keys : Done
  section MVP remaining
    OIDC providers : Next
    MFA TOTP : Next
  section Beyond MVP
    Passkeys / WebAuthn : Planned
    SAML + SCIM : Planned
    SMS OTP + risk scoring : Planned
```

---

## Status legend

| Badge meaning | Description |
|---|---|
| **Done** | Implemented and wired in the APIs |
| **Next** | Explicit next engineering iteration |
| **Later / Planned** | Product roadmap after MVP completion |

<p align="center">
  <a href="roadmap.md"><img src="https://img.shields.io/badge/EN--US-ACTIVE-0052CC?style=for-the-badge&logo=googletranslate&logoColor=white" alt="EN-US active" /></a>
  <a href="../pt-BR/roadmap.md"><img src="https://img.shields.io/badge/PT--BR-Switch-2E7D32?style=for-the-badge&logo=googletranslate&logoColor=white" alt="Switch to PT-BR" /></a>
</p>
