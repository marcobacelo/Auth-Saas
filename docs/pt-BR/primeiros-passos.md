# Primeiros passos

<p align="center">
  <a href="../en-US/getting-started.md"><img src="https://img.shields.io/badge/EN--US-Switch-0052CC?style=for-the-badge&logo=googletranslate&logoColor=white" alt="Switch to EN-US" /></a>
  <a href="primeiros-passos.md"><img src="https://img.shields.io/badge/PT--BR-ATIVO-2E7D32?style=for-the-badge&logo=googletranslate&logoColor=white" alt="PT-BR ativo" /></a>
</p>

<p align="center">
  <a href="README.md"><img src="https://img.shields.io/badge/🏠_Home-0B1F3A?style=for-the-badge" alt="Home" /></a>
  <a href="produto.md"><img src="https://img.shields.io/badge/📦_Produto-0B1F3A?style=for-the-badge" alt="Produto" /></a>
  <a href="stack.md"><img src="https://img.shields.io/badge/🧰_Stack-0B1F3A?style=for-the-badge" alt="Stack" /></a>
  <a href="arquitetura.md"><img src="https://img.shields.io/badge/🏗️_Arquitetura-0B1F3A?style=for-the-badge" alt="Arquitetura" /></a>
  <a href="roadmap.md"><img src="https://img.shields.io/badge/🗺️_Roadmap-0B1F3A?style=for-the-badge" alt="Roadmap" /></a>
  <a href="primeiros-passos.md"><img src="https://img.shields.io/badge/🚀_Primeiros%20passos-ATIVO-1565C0?style=for-the-badge" alt="Primeiros passos" /></a>
</p>

---

## Pré-requisitos

<p align="center">
  <img src="https://img.shields.io/badge/Java-25%20Corretto-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java" />
  <img src="https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker" />
  <img src="https://img.shields.io/badge/Gradle-Wrapper-02303A?style=for-the-badge&logo=gradle&logoColor=white" alt="Gradle" />
</p>

- Java 25 (Amazon Corretto recomendado)
- Docker + Docker Compose
- Clone do repositório com Gradle Wrapper

## 1. Subir a infraestrutura

```bash
docker compose -f deploy/docker-compose.yml up -d
```

Sobe **PostgreSQL** e **Redis**.

## 2. Rodar os planos

```bash
./gradlew :auth-control-plane:bootRun
./gradlew :auth-data-plane:bootRun
```

| Serviço | URL |
|---|---|
| Control plane | `http://localhost:8081` |
| Data plane | `http://localhost:8080` |

## 3. Onboard de um tenant

```bash
curl -u platform-admin:change-me -X POST http://localhost:8081/platform/v1/tenants \
  -H 'Content-Type: application/json' \
  -d '{"slug":"acme","displayName":"Acme Corp","adminUsername":"admin","adminPassword":"ChangeMeNow1!"}'
```

Cria o schema `t_acme`, roda migrations do tenant e semeia o usuário admin.

## 4. Login (senha)

```bash
curl -X POST http://localhost:8080/t/acme/v1/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"admin","password":"ChangeMeNow1!"}'
```

A resposta inclui `accessToken`, `refreshToken` e TTLs.

## 5. Refresh / logout

```bash
# Refresh (rotaciona o refresh token)
curl -X POST http://localhost:8080/t/acme/v1/auth/token/refresh \
  -H 'Content-Type: application/json' \
  -d '{"refreshToken":"<token>"}'

# Logout (revoga o refresh)
curl -X POST http://localhost:8080/t/acme/v1/auth/logout \
  -H 'Content-Type: application/json' \
  -d '{"refreshToken":"<token>"}'
```

## Endpoints úteis

| Método | Path | Plano |
|---|---|---|
| `POST` | `/platform/v1/tenants` | Control |
| `POST` | `/t/{slug}/v1/auth/login` | Data |
| `POST` | `/t/{slug}/v1/auth/api-key` | Data |
| `POST` | `/t/{slug}/v1/auth/token/refresh` | Data |
| `POST` | `/t/{slug}/v1/auth/logout` | Data |
| `GET` | `/t/{slug}/v1/.well-known/jwks.json` | Data |
| `GET` | `/t/{slug}/v1/auth/oauth2/{provider}/authorize` | Data (stub 501) |
| `POST` | `/t/{slug}/v1/auth/mfa/totp/verify` | Data (stub 501) |

## Próximas leituras

- [Produto](produto.md)
- [Arquitetura](arquitetura.md)
- [Roadmap](roadmap.md)

<p align="center">
  <a href="../en-US/getting-started.md"><img src="https://img.shields.io/badge/EN--US-Switch-0052CC?style=for-the-badge&logo=googletranslate&logoColor=white" alt="Switch to EN-US" /></a>
  <a href="primeiros-passos.md"><img src="https://img.shields.io/badge/PT--BR-ATIVO-2E7D32?style=for-the-badge&logo=googletranslate&logoColor=white" alt="PT-BR ativo" /></a>
</p>
