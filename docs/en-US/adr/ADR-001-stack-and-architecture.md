# ADR-001 — Stack and Architecture

> Language: **EN-US** | [PT-BR](../../pt-BR/adr/ADR-001-stack-and-architecture.md)

**Status:** Accepted  
**Date:** 2026-07-25  
**GroupId:** `com.auth.saas`

## Context

We are rewriting this repository into a commercial **multi-tenant Auth SaaS** platform. The product must be highly performant, support multiple authentication methods, isolate each customer (tenant) with schema-per-tenant, and expose a dedicated onboarding API.

The previous codebase was a single-tenant JWT demo on Java 21 + Spring Boot 3.2 (already outside OSS support). Nothing from that design is preserved.

## Decision

### Runtime and framework

| Layer | Choice | Rationale |
|---|---|---|
| JDK | **Java 25 LTS (Amazon Corretto)** | Current Java LTS; Spring Framework 7 recommends JDK 25 for production; Corretto is production-hardened and AWS-friendly |
| Application framework | **Spring Boot 4.1.x** (Spring Framework 7.0.x) | Current supported Boot line; opinionated packaging, Actuator, Security, Data, Flyway starters |
| Build | **Gradle 9.x + Kotlin DSL** | Java 25 support; typed, maintainable multi-module builds |
| Database | **PostgreSQL 16/17** | First-class schemas; strong fit for schema-per-tenant |
| Cache / ephemeral state | **Redis 7+** | Refresh tokens, rate limits, lockouts |
| Migrations | **Flyway** (platform + tenant) | Repeatable provisioning of tenant schemas |
| Password hashing | **Argon2id** | OWASP-recommended, memory-hard; replaces BCrypt |

### Spring Boot vs Spring Framework-only vs other Spring distributions

**We choose Spring Boot (not Framework-only, not Spring Cloud Gateway-as-core, not raw Authorization Server alone).**

| Option | Verdict | Why |
|---|---|---|
| **Spring Framework only** | Rejected for this product | You would re-implement Boot’s auto-config, Actuator, externalized config, fat-jar packaging, embedded Tomcat wiring, and starter curation. That cost does not buy meaningful performance for an Auth API. |
| **Spring Boot** | **Selected** | Correct abstraction for two deployable servers (control plane + data plane). Gives Security/OAuth2/Data/Flyway/Actuator with supported upgrade cadence. |
| **Spring Authorization Server alone** | Insufficient as the product shell | Excellent protocol engine for OAuth2/OIDC *inside* the data plane later; it is not a multi-tenant control plane, onboarding system, or SaaS product boundary. |
| **Spring Cloud** | Optional later | Useful for service discovery/config/gateway at scale; not required for v1 if we deploy two explicit services. |

Boot is not “heavier Framework with magic.” It is the supported distribution model for production Spring apps. Framework remains the core; Boot is how we assemble, configure, observe, and ship it.

### High-level architecture

```text
                 +----------------------+
                 |   Control Plane      |  onboarding, tenant admin
                 |   (separate deploy)  |
                 +----------+-----------+
                            | provisions schema + config
                            v
                 +----------------------+
                 |  Platform database   |  tenants catalog
                 +----------+-----------+
                            |
        +-------------------+-------------------+
        v                   v                   v
  schema t_acme       schema t_globex     schema t_...
        ^                   ^
        +---------+---------+
                  | tenant routing
                  v
         +----------------------+
         |     Data Plane       |  login, tokens, JWKS, MFA
         |  (separate deploy)   |
         +----------+-----------+
                    |
                    v
                 Redis
```

Modules:

- `auth-domain` — language/framework-agnostic contracts and models
- `auth-persistence` — platform + tenant persistence, schema routing, Flyway
- `auth-control-plane` — onboarding / admin HTTP API (deployable)
- `auth-data-plane` — authentication HTTP API (deployable)

### Multi-tenancy

- **v1:** schema-per-tenant on a shared PostgreSQL cluster
- **later:** dedicated database per enterprise plan (same routing abstraction)
- Onboarding creates schema `t_<slug>`, runs tenant migrations, seeds admin + default providers

### Authentication MVP (v1)

1. Password (Argon2id)
2. Refresh token (rotating) + revoke
3. OIDC/OAuth2 (Google, Microsoft, custom IdP)
4. API keys (machine clients)
5. MFA TOTP

Roadmap: Passkeys/WebAuthn, SAML, SMS OTP, SCIM, risk scoring.

### Documentation policy

- Source code and identifiers: **en-US**
- Product/architecture docs: **PT-BR and EN-US** Markdown under `docs/`
- Every Markdown page must expose a language switch link to its counterpart

## Consequences

- Full repository rewrite under `com.auth.saas`
- Two independently scalable processes from day one
- Operational need for tenant migration tooling and connection-pool discipline
- Must stay current on Spring Boot minors (Boot has no LTS)

## References

- Oracle Java SE Support Roadmap (JDK 25 LTS)
- Spring Boot Supported Versions / 4.0 Migration Guide
- Spring Framework 7.0 Release Notes (JDK 25 recommendation)
- OWASP Password Storage Cheat Sheet (Argon2id)
