# Architecture

<p align="center">
  <a href="architecture.md"><img src="https://img.shields.io/badge/EN--US-ACTIVE-0052CC?style=for-the-badge&logo=googletranslate&logoColor=white" alt="EN-US active" /></a>
  <a href="../pt-BR/arquitetura.md"><img src="https://img.shields.io/badge/PT--BR-Switch-2E7D32?style=for-the-badge&logo=googletranslate&logoColor=white" alt="Switch to PT-BR" /></a>
</p>

<p align="center">
  <a href="../../README.md"><img src="https://img.shields.io/badge/🏠_Home-0B1F3A?style=for-the-badge" alt="Home" /></a>
  <a href="product.md"><img src="https://img.shields.io/badge/📦_Product-0B1F3A?style=for-the-badge" alt="Product" /></a>
  <a href="stack.md"><img src="https://img.shields.io/badge/🧰_Stack-0B1F3A?style=for-the-badge" alt="Stack" /></a>
  <a href="architecture.md"><img src="https://img.shields.io/badge/🏗️_Architecture-ACTIVE-1565C0?style=for-the-badge" alt="Architecture" /></a>
  <a href="roadmap.md"><img src="https://img.shields.io/badge/🗺️_Roadmap-0B1F3A?style=for-the-badge" alt="Roadmap" /></a>
  <a href="getting-started.md"><img src="https://img.shields.io/badge/🚀_Getting%20Started-0B1F3A?style=for-the-badge" alt="Getting Started" /></a>
</p>

---

## High-level design

Auth SaaS separates **tenant administration** from **authentication traffic**.

```text
                 +----------------------+
                 |   Control Plane      |  onboarding, tenant admin
                 |   (deploy separate)  |  :8081
                 +----------+-----------+
                            | provision schema + config
                            v
                 +----------------------+
                 |  Platform database   |  tenant catalog
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
         |  (deploy separate)   |  :8080
         +----------+-----------+
                    |
                    v
                 Redis
```

<p align="center">
  <img src="https://img.shields.io/badge/Control%20Plane-8081-0052CC?style=for-the-badge&logo=springboot&logoColor=white" alt="Control Plane" />
  <img src="https://img.shields.io/badge/Data%20Plane-8080-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Data Plane" />
  <img src="https://img.shields.io/badge/PostgreSQL-Schemas-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL" />
  <img src="https://img.shields.io/badge/Redis-Refresh-DC382D?style=for-the-badge&logo=redis&logoColor=white" alt="Redis" />
</p>

## Modules

| Module | Deployable? | Responsibility |
|---|---|---|
| `auth-domain` | No | Framework-agnostic contracts and models |
| `auth-persistence` | No | Platform + tenant persistence, schema routing, Flyway |
| `auth-control-plane` | Yes (`:8081`) | HTTP onboarding / admin API |
| `auth-data-plane` | Yes (`:8080`) | HTTP authentication / token API |

```mermaid
flowchart LR
  CP[auth-control-plane] --> PERS[auth-persistence]
  DP[auth-data-plane] --> PERS
  PERS --> DOM[auth-domain]
  CP --> DOM
  DP --> DOM
  PERS --> PG[(PostgreSQL)]
  DP --> REDIS[(Redis)]
```

## Multi-tenancy model

| Phase | Model |
|---|---|
| **v1 (current)** | Schema-per-tenant on a shared PostgreSQL cluster |
| **Later** | Dedicated database on enterprise plans (same routing abstraction) |

Onboarding flow:

1. Validate slug / display name / admin credentials
2. Insert tenant in platform catalog
3. Create schema `t_<slug>`
4. Run tenant Flyway migrations
5. Seed admin identity + default auth provider configs

## Request routing (data plane)

Tenant is resolved from the path prefix:

```text
/t/{tenantSlug}/v1/auth/...
```

A filter loads the tenant, sets schema context, then auth providers and token services operate inside that tenant boundary.

## Security building blocks

| Concern | Approach |
|---|---|
| Passwords | Argon2id |
| Access tokens | JWT (signed), exposed via JWKS |
| Refresh tokens | Opaque tokens in Redis, rotate on use, revoke on logout |
| Control plane access | HTTP basic (platform admin) for v1 onboarding |
| Provider flags | Per-tenant `auth_provider_configs` (enable/disable methods) |

## Related pages

- [Stack](stack.md)
- [Roadmap](roadmap.md)
- [ADR-001](adr/ADR-001-stack-and-architecture.md)

<p align="center">
  <a href="architecture.md"><img src="https://img.shields.io/badge/EN--US-ACTIVE-0052CC?style=for-the-badge&logo=googletranslate&logoColor=white" alt="EN-US active" /></a>
  <a href="../pt-BR/arquitetura.md"><img src="https://img.shields.io/badge/PT--BR-Switch-2E7D32?style=for-the-badge&logo=googletranslate&logoColor=white" alt="Switch to PT-BR" /></a>
</p>
