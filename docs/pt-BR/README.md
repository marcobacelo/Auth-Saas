# Auth SaaS

<p align="center">
  <img src="https://img.shields.io/badge/Auth%20SaaS-Plataforma%20de%20Identidade%20Multi--tenant-0B1F3A?style=for-the-badge&labelColor=06101C" alt="Auth SaaS" />
</p>

<p align="center">
  <a href="../../README.md"><img src="https://img.shields.io/badge/EN--US-Switch-0052CC?style=for-the-badge&logo=googletranslate&logoColor=white" alt="Switch to EN-US" /></a>
  <a href="README.md"><img src="https://img.shields.io/badge/PT--BR-ATIVO-2E7D32?style=for-the-badge&logo=googletranslate&logoColor=white" alt="PT-BR ativo" /></a>
</p>

<p align="center">
  <b>Plataforma comercial de autenticação multi-tenant</b><br/>
  Provisione tenants. Autentique usuários. Emita tokens. Escale control plane e data plane de forma independente.
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

## Navegação

| Página | Descrição |
|---|---|
| [Produto](produto.md) | O que é o Auth SaaS, para quem é e a proposta de valor |
| [Stack](stack.md) | Tecnologias, versões e o porquê de cada escolha |
| [Arquitetura](arquitetura.md) | Control plane, data plane, schema-por-tenant, módulos |
| [Roadmap](roadmap.md) | Implementado vs planejado — MVP e além |
| [Primeiros passos](primeiros-passos.md) | Subir local, onboard de tenant, login |
| [ADR-001](adr/ADR-001-stack-and-architecture.md) | Registro de decisão de arquitetura |

<p align="center">
  <a href="produto.md"><img src="https://img.shields.io/badge/📦_Produto-0B1F3A?style=for-the-badge" alt="Produto" /></a>
  <a href="stack.md"><img src="https://img.shields.io/badge/🧰_Stack-0B1F3A?style=for-the-badge" alt="Stack" /></a>
  <a href="arquitetura.md"><img src="https://img.shields.io/badge/🏗️_Arquitetura-0B1F3A?style=for-the-badge" alt="Arquitetura" /></a>
  <a href="roadmap.md"><img src="https://img.shields.io/badge/🗺️_Roadmap-0B1F3A?style=for-the-badge" alt="Roadmap" /></a>
  <a href="primeiros-passos.md"><img src="https://img.shields.io/badge/🚀_Primeiros%20passos-0B1F3A?style=for-the-badge" alt="Primeiros passos" /></a>
</p>

---

## Produto em um olhar

Auth SaaS (`com.auth.saas`) é um backend de identidade para produtos SaaS B2B que precisam de:

- **Isolamento de tenant** — schema-por-tenant no PostgreSQL
- **Planos separados** — onboarding/admin vs tráfego de autenticação
- **Credenciais modernas** — senhas Argon2id, refresh rotativo, API keys
- **Superfície extensível** — OIDC e MFA TOTP já scaffoldados para a próxima iteração

| Plano | Porta | Responsabilidade |
|---|---|---|
| Control plane | `:8081` | Onboarding de tenant e admin da plataforma |
| Data plane | `:8080` | Login, tokens, JWKS, métodos de auth |

---

## Status de implementação

| Capacidade | Status |
|---|---|
| Plataforma multi-módulo Gradle | Feito |
| API de onboarding de tenant | Feito |
| Schema-por-tenant + Flyway | Feito |
| Login por senha (Argon2id) | Feito |
| Refresh rotativo / revoke (Redis) | Feito |
| Autenticação por API key | Feito |
| Endpoint JWKS | Feito |
| Provedores OIDC / OAuth2 | Próximo |
| MFA TOTP | Próximo |
| Passkeys, SAML, SCIM, risk scoring | Depois |

Detalhe completo: **[Roadmap](roadmap.md)**.

---

## Início rápido

```bash
docker compose -f deploy/docker-compose.yml up -d
./gradlew :auth-control-plane:bootRun
./gradlew :auth-data-plane:bootRun
```

Onboard de tenant e login — veja **[Primeiros passos](primeiros-passos.md)**.

---

## Módulos

| Módulo | Papel |
|---|---|
| `auth-domain` | Contratos e modelos de domínio |
| `auth-persistence` | Persistência platform/tenant + provisioning |
| `auth-control-plane` | API de onboarding (`:8081`) |
| `auth-data-plane` | API de autenticação / tokens (`:8080`) |

---

## Hubs de documentação

- [Documentação EN-US](../en-US/README.md)
- [Documentação PT-BR](README.md)

<p align="center">
  <a href="../../README.md"><img src="https://img.shields.io/badge/EN--US-Switch-0052CC?style=for-the-badge&logo=googletranslate&logoColor=white" alt="Switch to EN-US" /></a>
  <a href="README.md"><img src="https://img.shields.io/badge/PT--BR-ATIVO-2E7D32?style=for-the-badge&logo=googletranslate&logoColor=white" alt="PT-BR ativo" /></a>
</p>
