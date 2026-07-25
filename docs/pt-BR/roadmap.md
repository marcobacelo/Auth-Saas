# Roadmap

<p align="center">
  <a href="../en-US/roadmap.md"><img src="https://img.shields.io/badge/EN--US-Switch-0052CC?style=for-the-badge&logo=googletranslate&logoColor=white" alt="Switch to EN-US" /></a>
  <a href="roadmap.md"><img src="https://img.shields.io/badge/PT--BR-ATIVO-2E7D32?style=for-the-badge&logo=googletranslate&logoColor=white" alt="PT-BR ativo" /></a>
</p>

<p align="center">
  <a href="README.md"><img src="https://img.shields.io/badge/🏠_Home-0B1F3A?style=for-the-badge" alt="Home" /></a>
  <a href="produto.md"><img src="https://img.shields.io/badge/📦_Produto-0B1F3A?style=for-the-badge" alt="Produto" /></a>
  <a href="stack.md"><img src="https://img.shields.io/badge/🧰_Stack-0B1F3A?style=for-the-badge" alt="Stack" /></a>
  <a href="arquitetura.md"><img src="https://img.shields.io/badge/🏗️_Arquitetura-0B1F3A?style=for-the-badge" alt="Arquitetura" /></a>
  <a href="roadmap.md"><img src="https://img.shields.io/badge/🗺️_Roadmap-ATIVO-1565C0?style=for-the-badge" alt="Roadmap" /></a>
  <a href="primeiros-passos.md"><img src="https://img.shields.io/badge/🚀_Primeiros%20passos-0B1F3A?style=for-the-badge" alt="Primeiros passos" /></a>
</p>

---

## Onde estamos

A reescrita da fundação está **mergeada em `main`**: Auth SaaS multi-módulo com control plane, data plane, schema-por-tenant e APIs core de auth.

<p align="center">
  <img src="https://img.shields.io/badge/Fase-Fundação-2E7D32?style=for-the-badge&logo=checkmarx&logoColor=white" alt="Fundação concluída" />
  <img src="https://img.shields.io/badge/Próximo-OIDC%20%2B%20MFA%20TOTP-FB8C00?style=for-the-badge&logo=openid&logoColor=white" alt="Próximo OIDC MFA" />
  <img src="https://img.shields.io/badge/Depois-Passkeys%20·%20SAML%20·%20SCIM-607D8B?style=for-the-badge&logo=yubico&logoColor=white" alt="Depois" />
</p>

---

## MVP v1 — checklist de autenticação

Alinhado com o [ADR-001](adr/ADR-001-stack-and-architecture.md).

| # | Capacidade | Status | Notas |
|---|---|---|---|
| 1 | Password (Argon2id) | Feito | Endpoint de login ativo |
| 2 | Refresh token (rotativo) + revoke | Feito | Baseado em Redis |
| 3 | OIDC / OAuth2 (Google, Microsoft, IdP custom) | Próximo | `OidcStubController` retorna 501 |
| 4 | API keys (clientes máquina) | Feito | Login por API key ativo |
| 5 | MFA TOTP | Próximo | `MfaTotpStubController` retorna 501 |

### Fundação da plataforma (também entregue)

| Capacidade | Status |
|---|---|
| Gradle multi-módulo sob `com.auth.saas` | Feito |
| Onboarding de tenant no control plane | Feito |
| Schema-por-tenant + provisioning Flyway | Feito |
| Endpoint JWKS | Feito |
| Docker Compose (Postgres + Redis) | Feito |
| CI (Corretto 25) | Feito |
| Docs bilingues + ADR-001 | Feito |

---

## Próxima iteração (imediata)

| Item | Objetivo |
|---|---|
| **Fluxo OIDC authorization-code** | `/oauth2/{provider}/authorize` + callback reais, config de IdP por tenant |
| **MFA TOTP** | Enroll + verify usando `mfa_totp_secrets`, exigir no login quando habilitado |

Isso substitui os stubs 501 scaffoldados no data plane.

---

## Roadmap pós-MVP

| Tema | Itens |
|---|---|
| Auth forte | Passkeys / WebAuthn |
| Federação enterprise | SAML |
| MFA alternativa | SMS OTP |
| Sync de diretório | SCIM |
| Segurança adaptativa | Risk scoring |
| Escala de tenancy | Database dedicado por tenant enterprise |

```mermaid
timeline
  title Entrega Auth SaaS
  section Fundação
    Reescrita da plataforma : Feito
    Password + Refresh + API keys : Feito
  section MVP restante
    Provedores OIDC : Próximo
    MFA TOTP : Próximo
  section Além do MVP
    Passkeys / WebAuthn : Planejado
    SAML + SCIM : Planejado
    SMS OTP + risk scoring : Planejado
```

---

## Legenda de status

| Significado | Descrição |
|---|---|
| **Feito** | Implementado e ligado nas APIs |
| **Próximo** | Próxima iteração explícita de engenharia |
| **Depois / Planejado** | Roadmap de produto após o MVP |

<p align="center">
  <a href="../en-US/roadmap.md"><img src="https://img.shields.io/badge/EN--US-Switch-0052CC?style=for-the-badge&logo=googletranslate&logoColor=white" alt="Switch to EN-US" /></a>
  <a href="roadmap.md"><img src="https://img.shields.io/badge/PT--BR-ATIVO-2E7D32?style=for-the-badge&logo=googletranslate&logoColor=white" alt="PT-BR ativo" /></a>
</p>
