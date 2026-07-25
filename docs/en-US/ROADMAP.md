# Roadmap

> Language: **EN-US** | [PT-BR](../pt-BR/ROADMAP.md)

Living backlog for `com.auth.saas`. Use this document to resume work in a new local IDE session.

## Current baseline (done)

- Multi-module Gradle Kotlin DSL rewrite under `com.auth.saas`
- Java 25 (Amazon Corretto) + Spring Boot 4.1 / Spring Framework 7
- Separated **control plane** and **data plane**
- Schema-per-tenant provisioning via Flyway
- Password login with **Argon2id**
- Refresh token issue/rotate/revoke (Redis)
- API key authentication
- JWKS endpoint
- OIDC and MFA TOTP scaffold endpoints (`501`)
- Bilingual docs with language switch links
- ADR-001 (stack + architecture)

## Next increments (priority order)

Resume here in the next session. Suggested order:

### 1. OIDC authorization-code (Google / Microsoft / custom IdP)

- Complete `/t/{tenant}/v1/auth/oauth2/{provider}/authorize` + callback
- Per-tenant provider config in `auth_provider_configs`
- Account linking by `external_subject`
- Emit JWT with `amr=oidc`

**Why first:** highest commercial value after password; stubs already exist.

### 2. MFA TOTP (enroll + verify in login flow)

- Secret enroll/QR for identities
- `/t/{tenant}/v1/auth/mfa/totp/verify` real implementation
- Optional/enforced policy per tenant
- Step-up challenge after password when MFA is enabled

**Why second:** security differentiator; table `mfa_totp_secrets` already provisioned.

### 3. API key management on control plane

- Create/list/revoke keys for a tenant identity
- Prefix + Argon2id hash storage (data plane verify already exists)
- Scopes/roles bound to keys

### 4. Tenant DataSource pool / cache under load

- LRU cache of Hikari pools or schema-aware borrowing strategy
- Idle eviction and max open tenants
- Metrics: pool wait, active schemas, auth latency

### 5. Integration tests with Testcontainers

- Postgres + Redis containers
- Onboarding → login → refresh → revoke happy path
- CI green with `./gradlew build`

## Later roadmap (post-MVP hardening)

| Item | Notes |
|---|---|
| Passkeys / WebAuthn | Passwordless enterprise UX |
| SAML 2.0 | Enterprise IdP federation |
| SMS OTP | Costly; provider-pluggable |
| SCIM | User provisioning for B2B |
| Risk scoring / brute-force | Lockout, velocity checks, webhooks |
| Dedicated DB per enterprise tenant | Same routing abstraction as schema-per-tenant |
| Spring Authorization Server (protocol engine) | Optional inside data plane for full OAuth2 AS |
| Key rotation / per-tenant JWKS | Move beyond single demo RSA key pair |

## Suggested local session bootstrap

```bash
git checkout cursor/auth-saas-platform-6918   # or main after merge
docker compose -f deploy/docker-compose.yml up -d
./gradlew build
./gradlew :auth-control-plane:bootRun
./gradlew :auth-data-plane:bootRun
```

Read first:

1. [ADR-001 — Stack and Architecture](adr/ADR-001-stack-and-architecture.md)
2. This roadmap
3. Stub controllers in `auth-data-plane` (`OidcStubController`, `MfaTotpStubController`)

## Tracking convention

When an item ships, move it from **Next increments** to **Current baseline** and keep bilingual docs in sync (`docs/en-US` ↔ `docs/pt-BR`).
