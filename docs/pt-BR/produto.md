# Produto

<p align="center">
  <a href="../en-US/product.md"><img src="https://img.shields.io/badge/EN--US-Switch-0052CC?style=for-the-badge&logo=googletranslate&logoColor=white" alt="Switch to EN-US" /></a>
  <a href="produto.md"><img src="https://img.shields.io/badge/PT--BR-ATIVO-2E7D32?style=for-the-badge&logo=googletranslate&logoColor=white" alt="PT-BR ativo" /></a>
</p>

<p align="center">
  <a href="README.md"><img src="https://img.shields.io/badge/🏠_Home-0B1F3A?style=for-the-badge" alt="Home" /></a>
  <a href="produto.md"><img src="https://img.shields.io/badge/📦_Produto-ATIVO-1565C0?style=for-the-badge" alt="Produto" /></a>
  <a href="stack.md"><img src="https://img.shields.io/badge/🧰_Stack-0B1F3A?style=for-the-badge" alt="Stack" /></a>
  <a href="arquitetura.md"><img src="https://img.shields.io/badge/🏗️_Arquitetura-0B1F3A?style=for-the-badge" alt="Arquitetura" /></a>
  <a href="roadmap.md"><img src="https://img.shields.io/badge/🗺️_Roadmap-0B1F3A?style=for-the-badge" alt="Roadmap" /></a>
  <a href="primeiros-passos.md"><img src="https://img.shields.io/badge/🚀_Primeiros%20passos-0B1F3A?style=for-the-badge" alt="Primeiros passos" /></a>
</p>

---

## O que é o Auth SaaS?

Auth SaaS é uma **plataforma comercial de autenticação multi-tenant** pensada para ser o backbone de identidade de produtos B2B.

Em vez de um demo JWT single-tenant, cada organização cliente é um tenant isolado com schema próprio, usuário admin e configuração de provedores de autenticação.

<p align="center">
  <img src="https://img.shields.io/badge/🎯_Multi--tenant-0B1F3A?style=for-the-badge" alt="Multi-tenant" />
  <img src="https://img.shields.io/badge/🔐_API%20de%20Auth-0B1F3A?style=for-the-badge" alt="API de Auth" />
  <img src="https://img.shields.io/badge/🏢_SaaS%20B2B-0B1F3A?style=for-the-badge" alt="SaaS B2B" />
  <img src="https://img.shields.io/badge/⚡_Dois%20planos-0B1F3A?style=for-the-badge" alt="Dois planos" />
</p>

## Para quem é

| Público | Por que encaixa |
|---|---|
| Founders / times de plataforma SaaS | Precisam de auth com tenant sem construir um IdP do zero |
| Engenheiros backend | Módulos claros, deployáveis Spring Boot, isolamento por schema |
| Times de segurança | Argon2id, refresh rotativo, admin separado do tráfego de auth |

## Proposta de valor

1. **Isolamento de tenant por desenho** — cada tenant recebe schema `t_<slug>`, migrations, admin e providers default.
2. **Escala independente** — control plane (onboarding) e data plane (login/tokens) são processos separados.
3. **Métodos de auth como superfície de produto** — senha, refresh e API keys hoje; OIDC e MFA em seguida; passkeys/SAML depois.
4. **Stack amigável a operação** — PostgreSQL, Redis, Flyway, Docker Compose, CI com Corretto 25.

## Superfície de produto (API)

| Capacidade | Plano | Status |
|---|---|---|
| Criar / onboard de tenant | Control (`:8081`) | Disponível |
| Login por senha | Data (`:8080`) | Disponível |
| Refresh rotativo + revoke | Data | Disponível |
| Login por API key | Data | Disponível |
| JWKS | Data | Disponível |
| Authorize OIDC | Data | Scaffold (501) |
| Verify MFA TOTP | Data | Scaffold (501) |

## Posicionamento

```text
Seu produto     ──►  Auth SaaS Data Plane    (usuários autenticam aqui)
Ops da plataforma ──►  Auth SaaS Control Plane (tenants são provisionados aqui)
```

Auth SaaS **ainda não** é um console IdP completo com UI para o cliente final. É o **produto API**: onboarding + autenticação + tokens, pronto para ficar atrás dos seus apps e gateways.

## Páginas relacionadas

- [Arquitetura](arquitetura.md) — como planos e tenancy funcionam
- [Roadmap](roadmap.md) — o que vem a seguir
- [Primeiros passos](primeiros-passos.md) — experimente localmente

<p align="center">
  <a href="../en-US/product.md"><img src="https://img.shields.io/badge/EN--US-Switch-0052CC?style=for-the-badge&logo=googletranslate&logoColor=white" alt="Switch to EN-US" /></a>
  <a href="produto.md"><img src="https://img.shields.io/badge/PT--BR-ATIVO-2E7D32?style=for-the-badge&logo=googletranslate&logoColor=white" alt="PT-BR ativo" /></a>
</p>
