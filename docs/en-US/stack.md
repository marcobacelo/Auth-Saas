# Stack

<p align="center">
  <a href="stack.md"><img src="https://img.shields.io/badge/EN--US-ACTIVE-0052CC?style=for-the-badge&logo=googletranslate&logoColor=white" alt="EN-US active" /></a>
  <a href="../pt-BR/stack.md"><img src="https://img.shields.io/badge/PT--BR-Switch-2E7D32?style=for-the-badge&logo=googletranslate&logoColor=white" alt="Switch to PT-BR" /></a>
</p>

<p align="center">
  <a href="../../README.md"><img src="https://img.shields.io/badge/🏠_Home-0B1F3A?style=for-the-badge" alt="Home" /></a>
  <a href="product.md"><img src="https://img.shields.io/badge/📦_Product-0B1F3A?style=for-the-badge" alt="Product" /></a>
  <a href="stack.md"><img src="https://img.shields.io/badge/🧰_Stack-ACTIVE-1565C0?style=for-the-badge" alt="Stack" /></a>
  <a href="architecture.md"><img src="https://img.shields.io/badge/🏗️_Architecture-0B1F3A?style=for-the-badge" alt="Architecture" /></a>
  <a href="roadmap.md"><img src="https://img.shields.io/badge/🗺️_Roadmap-0B1F3A?style=for-the-badge" alt="Roadmap" /></a>
  <a href="getting-started.md"><img src="https://img.shields.io/badge/🚀_Getting%20Started-0B1F3A?style=for-the-badge" alt="Getting Started" /></a>
</p>

---

## Technology strip

<p align="center">
  <img src="https://img.shields.io/badge/Java-25%20LTS-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java" />
  <img src="https://img.shields.io/badge/Amazon%20Corretto-25-FF9900?style=for-the-badge&logo=amazon&logoColor=white" alt="Corretto" />
  <img src="https://img.shields.io/badge/Spring%20Boot-4.1.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Spring%20Framework-7-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Framework" />
  <img src="https://img.shields.io/badge/Gradle-9%20Kotlin%20DSL-02303A?style=for-the-badge&logo=gradle&logoColor=white" alt="Gradle" />
</p>

<p align="center">
  <img src="https://img.shields.io/badge/PostgreSQL-17-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL" />
  <img src="https://img.shields.io/badge/Redis-7-DC382D?style=for-the-badge&logo=redis&logoColor=white" alt="Redis" />
  <img src="https://img.shields.io/badge/Flyway-Migrations-CC0200?style=for-the-badge&logo=flyway&logoColor=white" alt="Flyway" />
  <img src="https://img.shields.io/badge/Argon2id-Password%20hash-1B1F23?style=for-the-badge&logo=let'sencrypt&logoColor=white" alt="Argon2id" />
  <img src="https://img.shields.io/badge/JWT%20%2F%20JWKS-OAuth2-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white" alt="JWT" />
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker" />
  <img src="https://img.shields.io/badge/GitHub%20Actions-CI-2088FF?style=for-the-badge&logo=githubactions&logoColor=white" alt="GitHub Actions" />
  <img src="https://img.shields.io/badge/Spring%20Security-OAuth2%20Resource-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white" alt="Spring Security" />
</p>

---

## Choices and rationale

| Layer | Choice | Why |
|---|---|---|
| JDK | **Java 25 LTS (Amazon Corretto)** | Current Java LTS; Spring Framework 7 recommends JDK 25; Corretto is production-hardened |
| App framework | **Spring Boot 4.1.x** | Supported packaging for two deployables — Security, Data, Flyway, Actuator |
| Build | **Gradle 9 + Kotlin DSL** | Java 25 support; typed multi-module builds |
| Database | **PostgreSQL 17** | First-class schemas for schema-per-tenant |
| Ephemeral state | **Redis 7** | Refresh tokens, future rate limits / lockouts |
| Migrations | **Flyway** | Repeatable platform + per-tenant provisioning |
| Password hashing | **Argon2id** | OWASP-recommended, memory-hard |

## Spring Boot (not Framework-only)

We chose **Spring Boot** as the distribution model:

| Option | Verdict |
|---|---|
| Spring Framework only | Rejected — too much reinvented packaging/ops |
| **Spring Boot** | **Selected** — correct abstraction for control + data planes |
| Spring Authorization Server alone | Insufficient as the whole SaaS shell |
| Spring Cloud | Optional later for gateway/discovery at scale |

Decision detail: [ADR-001](adr/ADR-001-stack-and-architecture.md).

## Runtime dependencies (local)

| Service | Image / tool | Used for |
|---|---|---|
| PostgreSQL | Docker Compose | Platform catalog + tenant schemas |
| Redis | Docker Compose | Refresh token store |
| Corretto 25 | Local / CI | JVM runtime |
| Gradle Wrapper | Repo | Builds and `bootRun` |

## Related pages

- [Architecture](architecture.md)
- [ADR-001](adr/ADR-001-stack-and-architecture.md)
- [Getting started](getting-started.md)

<p align="center">
  <a href="stack.md"><img src="https://img.shields.io/badge/EN--US-ACTIVE-0052CC?style=for-the-badge&logo=googletranslate&logoColor=white" alt="EN-US active" /></a>
  <a href="../pt-BR/stack.md"><img src="https://img.shields.io/badge/PT--BR-Switch-2E7D32?style=for-the-badge&logo=googletranslate&logoColor=white" alt="Switch to PT-BR" /></a>
</p>
