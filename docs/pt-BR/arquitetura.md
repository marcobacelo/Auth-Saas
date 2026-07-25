# Arquitetura

<p align="center">
  <a href="../en-US/architecture.md"><img src="https://img.shields.io/badge/EN--US-Switch-0052CC?style=for-the-badge&logo=googletranslate&logoColor=white" alt="Switch to EN-US" /></a>
  <a href="arquitetura.md"><img src="https://img.shields.io/badge/PT--BR-ATIVO-2E7D32?style=for-the-badge&logo=googletranslate&logoColor=white" alt="PT-BR ativo" /></a>
</p>

<p align="center">
  <a href="README.md"><img src="https://img.shields.io/badge/🏠_Home-0B1F3A?style=for-the-badge" alt="Home" /></a>
  <a href="produto.md"><img src="https://img.shields.io/badge/📦_Produto-0B1F3A?style=for-the-badge" alt="Produto" /></a>
  <a href="stack.md"><img src="https://img.shields.io/badge/🧰_Stack-0B1F3A?style=for-the-badge" alt="Stack" /></a>
  <a href="arquitetura.md"><img src="https://img.shields.io/badge/🏗️_Arquitetura-ATIVO-1565C0?style=for-the-badge" alt="Arquitetura" /></a>
  <a href="roadmap.md"><img src="https://img.shields.io/badge/🗺️_Roadmap-0B1F3A?style=for-the-badge" alt="Roadmap" /></a>
  <a href="primeiros-passos.md"><img src="https://img.shields.io/badge/🚀_Primeiros%20passos-0B1F3A?style=for-the-badge" alt="Primeiros passos" /></a>
</p>

---

## Desenho de alto nível

Auth SaaS separa **administração de tenants** do **tráfego de autenticação**.

```text
                 +----------------------+
                 |   Control Plane      |  onboarding, admin de tenant
                 |   (deploy separado)  |  :8081
                 +----------+-----------+
                            | provisiona schema + config
                            v
                 +----------------------+
                 |  Banco platform      |  catálogo de tenants
                 +----------+-----------+
                            |
        +-------------------+-------------------+
        v                   v                   v
  schema t_acme       schema t_globex     schema t_...
        ^                   ^
        +---------+---------+
                  | roteamento por tenant
                  v
         +----------------------+
         |     Data Plane       |  login, tokens, JWKS, MFA
         |  (deploy separado)   |  :8080
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

## Módulos

| Módulo | Deployável? | Responsabilidade |
|---|---|---|
| `auth-domain` | Não | Contratos e modelos agnósticos de framework |
| `auth-persistence` | Não | Persistência platform + tenant, roteamento de schema, Flyway |
| `auth-control-plane` | Sim (`:8081`) | API HTTP de onboarding / admin |
| `auth-data-plane` | Sim (`:8080`) | API HTTP de autenticação / tokens |

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

## Modelo multi-tenant

| Fase | Modelo |
|---|---|
| **v1 (atual)** | Schema-por-tenant em um cluster PostgreSQL compartilhado |
| **Depois** | Database dedicado no plano enterprise (mesma abstração de roteamento) |

Fluxo de onboarding:

1. Validar slug / nome / credenciais do admin
2. Inserir tenant no catálogo platform
3. Criar schema `t_<slug>`
4. Rodar migrations Flyway do tenant
5. Semear identity admin + configs default de providers

## Roteamento de request (data plane)

O tenant é resolvido pelo prefixo de path:

```text
/t/{tenantSlug}/v1/auth/...
```

Um filtro carrega o tenant, define o contexto de schema e então providers/tokens operam dentro dessa fronteira.

## Blocos de segurança

| Preocupação | Abordagem |
|---|---|
| Senhas | Argon2id |
| Access tokens | JWT assinado, exposto via JWKS |
| Refresh tokens | Tokens opacos no Redis, rotacionam no uso, revoke no logout |
| Acesso ao control plane | HTTP basic (platform admin) no onboarding v1 |
| Flags de provider | `auth_provider_configs` por tenant (liga/desliga métodos) |

## Páginas relacionadas

- [Stack](stack.md)
- [Roadmap](roadmap.md)
- [ADR-001](adr/ADR-001-stack-and-architecture.md)

<p align="center">
  <a href="../en-US/architecture.md"><img src="https://img.shields.io/badge/EN--US-Switch-0052CC?style=for-the-badge&logo=googletranslate&logoColor=white" alt="Switch to EN-US" /></a>
  <a href="arquitetura.md"><img src="https://img.shields.io/badge/PT--BR-ATIVO-2E7D32?style=for-the-badge&logo=googletranslate&logoColor=white" alt="PT-BR ativo" /></a>
</p>
