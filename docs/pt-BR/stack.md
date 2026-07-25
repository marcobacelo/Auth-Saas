# Stack

<p align="center">
  <a href="../en-US/stack.md"><img src="https://img.shields.io/badge/EN--US-Switch-0052CC?style=for-the-badge&logo=googletranslate&logoColor=white" alt="Switch to EN-US" /></a>
  <a href="stack.md"><img src="https://img.shields.io/badge/PT--BR-ATIVO-2E7D32?style=for-the-badge&logo=googletranslate&logoColor=white" alt="PT-BR ativo" /></a>
</p>

<p align="center">
  <a href="README.md"><img src="https://img.shields.io/badge/🏠_Home-0B1F3A?style=for-the-badge" alt="Home" /></a>
  <a href="produto.md"><img src="https://img.shields.io/badge/📦_Produto-0B1F3A?style=for-the-badge" alt="Produto" /></a>
  <a href="stack.md"><img src="https://img.shields.io/badge/🧰_Stack-ATIVO-1565C0?style=for-the-badge" alt="Stack" /></a>
  <a href="arquitetura.md"><img src="https://img.shields.io/badge/🏗️_Arquitetura-0B1F3A?style=for-the-badge" alt="Arquitetura" /></a>
  <a href="roadmap.md"><img src="https://img.shields.io/badge/🗺️_Roadmap-0B1F3A?style=for-the-badge" alt="Roadmap" /></a>
  <a href="primeiros-passos.md"><img src="https://img.shields.io/badge/🚀_Primeiros%20passos-0B1F3A?style=for-the-badge" alt="Primeiros passos" /></a>
</p>

---

## Faixa de tecnologias

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
  <img src="https://img.shields.io/badge/Argon2id-Hash%20de%20senha-1B1F23?style=for-the-badge&logo=let'sencrypt&logoColor=white" alt="Argon2id" />
  <img src="https://img.shields.io/badge/JWT%20%2F%20JWKS-OAuth2-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white" alt="JWT" />
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker" />
  <img src="https://img.shields.io/badge/GitHub%20Actions-CI-2088FF?style=for-the-badge&logo=githubactions&logoColor=white" alt="GitHub Actions" />
  <img src="https://img.shields.io/badge/Spring%20Security-OAuth2%20Resource-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white" alt="Spring Security" />
</p>

---

## Escolhas e justificativa

| Camada | Escolha | Por quê |
|---|---|---|
| JDK | **Java 25 LTS (Amazon Corretto)** | LTS atual do Java; Spring Framework 7 recomenda JDK 25; Corretto endurecido para produção |
| Framework de app | **Spring Boot 4.1.x** | Packaging suportado para dois deployáveis — Security, Data, Flyway, Actuator |
| Build | **Gradle 9 + Kotlin DSL** | Suporte a Java 25; builds multi-módulo tipados |
| Banco | **PostgreSQL 17** | Schemas de primeira classe para schema-por-tenant |
| Estado efêmero | **Redis 7** | Refresh tokens; no futuro rate limits / lockouts |
| Migrations | **Flyway** | Provisionamento repetível platform + por tenant |
| Hash de senha | **Argon2id** | Recomendação OWASP, memory-hard |

## Spring Boot (não só Framework)

Escolhemos **Spring Boot** como modelo de distribuição:

| Opção | Veredito |
|---|---|
| Somente Spring Framework | Rejeitado — reinventaria packaging/ops |
| **Spring Boot** | **Selecionado** — abstração correta para control + data plane |
| Só Spring Authorization Server | Insuficiente como casca do SaaS |
| Spring Cloud | Opcional depois para gateway/discovery em escala |

Detalhe da decisão: [ADR-001](adr/ADR-001-stack-and-architecture.md).

## Dependências de runtime (local)

| Serviço | Imagem / ferramenta | Uso |
|---|---|---|
| PostgreSQL | Docker Compose | Catálogo platform + schemas de tenant |
| Redis | Docker Compose | Store de refresh tokens |
| Corretto 25 | Local / CI | Runtime JVM |
| Gradle Wrapper | Repo | Builds e `bootRun` |

## Páginas relacionadas

- [Arquitetura](arquitetura.md)
- [ADR-001](adr/ADR-001-stack-and-architecture.md)
- [Primeiros passos](primeiros-passos.md)

<p align="center">
  <a href="../en-US/stack.md"><img src="https://img.shields.io/badge/EN--US-Switch-0052CC?style=for-the-badge&logo=googletranslate&logoColor=white" alt="Switch to EN-US" /></a>
  <a href="stack.md"><img src="https://img.shields.io/badge/PT--BR-ATIVO-2E7D32?style=for-the-badge&logo=googletranslate&logoColor=white" alt="PT-BR ativo" /></a>
</p>
