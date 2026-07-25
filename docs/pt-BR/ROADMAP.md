# Roadmap

> Idioma: **PT-BR** | [EN-US](../en-US/ROADMAP.md)

Backlog vivo do `com.auth.saas`. Use este documento para retomar o trabalho em uma nova sessão local da IDE.

## Baseline atual (feito)

- Reescrita multi-módulo Gradle Kotlin DSL sob `com.auth.saas`
- Java 25 (Amazon Corretto) + Spring Boot 4.1 / Spring Framework 7
- **Control plane** e **data plane** separados
- Provisionamento schema-por-tenant via Flyway
- Login com senha usando **Argon2id**
- Refresh token emitir/rotacionar/revogar (Redis)
- Autenticação por API key
- Endpoint JWKS
- Endpoints scaffold de OIDC e MFA TOTP (`501`)
- Docs bilingues com troca de idioma
- ADR-001 (stack + arquitetura)

## Próximos incrementos (ordem de prioridade)

Retome por aqui na próxima sessão. Ordem sugerida:

### 1. OIDC authorization-code (Google / Microsoft / IdP custom)

- Completar `/t/{tenant}/v1/auth/oauth2/{provider}/authorize` + callback
- Config de provider por tenant em `auth_provider_configs`
- Vinculação de conta por `external_subject`
- Emitir JWT com `amr=oidc`

**Por que primeiro:** maior valor comercial após password; stubs já existem.

### 2. MFA TOTP (enroll + verify no fluxo de login)

- Enroll/QR do secret por identity
- Implementação real de `/t/{tenant}/v1/auth/mfa/totp/verify`
- Política opcional/obrigatória por tenant
- Desafio step-up após password quando MFA estiver habilitado

**Por que segundo:** diferencial de segurança; tabela `mfa_totp_secrets` já é provisionada.

### 3. Gestão de API keys no control plane

- Criar/listar/revogar keys de uma identity do tenant
- Armazenamento prefix + hash Argon2id (verify no data plane já existe)
- Scopes/roles ligados às keys

### 4. Pool/cache de DataSources por tenant sob carga

- Cache LRU de pools Hikari ou estratégia schema-aware
- Eviction de idle e limite de tenants abertos
- Métricas: espera de pool, schemas ativos, latência de auth

### 5. Testes de integração com Testcontainers

- Containers Postgres + Redis
- Happy path onboarding → login → refresh → revoke
- CI verde com `./gradlew build`

## Roadmap posterior (endurecimento pós-MVP)

| Item | Notas |
|---|---|
| Passkeys / WebAuthn | UX passwordless enterprise |
| SAML 2.0 | Federação com IdP corporativo |
| SMS OTP | Custo variável; pluggable por provider |
| SCIM | Provisionamento de usuários B2B |
| Risk scoring / brute-force | Lockout, velocity checks, webhooks |
| DB dedicado por tenant enterprise | Mesma abstração de roteamento do schema-por-tenant |
| Spring Authorization Server (motor de protocolo) | Opcional dentro do data plane para OAuth2 AS completo |
| Rotação de chaves / JWKS por tenant | Ir além do par RSA demo único |

## Bootstrap sugerido para sessão local

```bash
git checkout cursor/auth-saas-platform-6918   # ou main após merge
docker compose -f deploy/docker-compose.yml up -d
./gradlew build
./gradlew :auth-control-plane:bootRun
./gradlew :auth-data-plane:bootRun
```

Leia primeiro:

1. [ADR-001 — Stack e Arquitetura](adr/ADR-001-stack-and-architecture.md)
2. Este roadmap
3. Controllers stub em `auth-data-plane` (`OidcStubController`, `MfaTotpStubController`)

## Convenção de tracking

Quando um item for entregue, mova-o de **Próximos incrementos** para **Baseline atual** e mantenha os docs bilingues sincronizados (`docs/en-US` ↔ `docs/pt-BR`).
