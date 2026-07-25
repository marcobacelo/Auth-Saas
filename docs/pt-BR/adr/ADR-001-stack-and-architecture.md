# ADR-001 — Stack e Arquitetura

> Idioma: **PT-BR** | [EN-US](../../en-US/adr/ADR-001-stack-and-architecture.md)

**Status:** Aceito  
**Data:** 2026-07-25  
**GroupId:** `com.auth.saas`

## Contexto

Estamos reescrevendo este repositório como uma plataforma comercial de **Auth SaaS multi-tenant**. O produto precisa ser altamente performático, oferecer múltiplos métodos de autenticação, isolar cada cliente (tenant) com schema-por-tenant e expor uma API dedicada de onboarding.

O código anterior era um demo JWT single-tenant em Java 21 + Spring Boot 3.2 (já fora do suporte OSS). Nada desse desenho é preservado.

## Decisão

### Runtime e framework

| Camada | Escolha | Justificativa |
|---|---|---|
| JDK | **Java 25 LTS (Amazon Corretto)** | LTS atual do Java; Spring Framework 7 recomenda JDK 25 em produção; Corretto é endurecido para produção e amigável à AWS |
| Framework de aplicação | **Spring Boot 4.1.x** (Spring Framework 7.0.x) | Linha Boot suportada atualmente; packaging opinado, Actuator, Security, Data, Flyway |
| Build | **Gradle 9.x + Kotlin DSL** | Suporte a Java 25; builds multi-módulo tipados e manuteníveis |
| Banco | **PostgreSQL 16/17** | Schemas de primeira classe; ótimo encaixe para schema-por-tenant |
| Cache / estado efêmero | **Redis 7+** | Refresh tokens, rate limits, lockouts |
| Migrations | **Flyway** (platform + tenant) | Provisionamento repetível de schemas de tenant |
| Hash de senha | **Argon2id** | Recomendação OWASP, memory-hard; substitui BCrypt |

### Spring Boot vs somente Spring Framework vs outras distros

**Escolhemos Spring Boot (não Framework puro, não Spring Cloud Gateway como núcleo, não Authorization Server sozinho).**

| Opção | Veredito | Por quê |
|---|---|---|
| **Somente Spring Framework** | Rejeitado para este produto | Você reimplementaria auto-config do Boot, Actuator, config externalizada, fat-jar, wiring do Tomcat embutido e curadoria de starters. Esse custo não compra performance relevante para uma Auth API. |
| **Spring Boot** | **Selecionado** | Abstração correta para dois servidores deployáveis (control plane + data plane). Entrega Security/OAuth2/Data/Flyway/Actuator com cadência de upgrade suportada. |
| **Somente Spring Authorization Server** | Insuficiente como casca do produto | Excelente motor de protocolo OAuth2/OIDC *dentro* do data plane depois; não é control plane multi-tenant, onboarding nem fronteira de produto SaaS. |
| **Spring Cloud** | Opcional depois | Útil para discovery/config/gateway em escala; não é necessário na v1 se deployamos dois serviços explícitos. |

Boot não é “Framework mais pesado com magia”. É o modelo de distribuição suportado para apps Spring em produção. Framework continua sendo o núcleo; Boot é como montamos, configuramos, observamos e publicamos.

### Arquitetura de alto nível

```text
                 +----------------------+
                 |   Control Plane      |  onboarding, admin de tenant
                 |   (deploy separado)  |
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
         |  (deploy separado)   |
         +----------+-----------+
                    |
                    v
                 Redis
```

Módulos:

- `auth-domain` — contratos e modelos agnósticos de framework
- `auth-persistence` — persistência platform + tenant, roteamento de schema, Flyway
- `auth-control-plane` — API HTTP de onboarding/admin (deployável)
- `auth-data-plane` — API HTTP de autenticação (deployável)

### Multi-tenancy

- **v1:** schema-por-tenant em um cluster PostgreSQL compartilhado
- **depois:** database dedicado no plano enterprise (mesma abstração de roteamento)
- Onboarding cria schema `t_<slug>`, roda migrations do tenant, cria admin + providers default

### Autenticação MVP (v1)

1. Password (Argon2id)
2. Refresh token (rotativo) + revoke
3. OIDC/OAuth2 (Google, Microsoft, IdP custom)
4. API keys (clientes máquina)
5. MFA TOTP

Roadmap: Passkeys/WebAuthn, SAML, SMS OTP, SCIM, risk scoring.

### Política de documentação

- Código-fonte e identificadores: **en-US**
- Docs de produto/arquitetura: **PT-BR e EN-US** em Markdown sob `docs/`
- Toda página Markdown deve expor link de troca de idioma para a contraparte

## Consequências

- Reescrita total do repositório sob `com.auth.saas`
- Dois processos escaláveis independentemente desde o dia um
- Necessidade operacional de tooling de migration de tenant e disciplina de connection pool
- Precisamos acompanhar minors do Spring Boot (Boot não tem LTS)

## Referências

- Oracle Java SE Support Roadmap (JDK 25 LTS)
- Spring Boot Supported Versions / 4.0 Migration Guide
- Spring Framework 7.0 Release Notes (recomendação JDK 25)
- OWASP Password Storage Cheat Sheet (Argon2id)
