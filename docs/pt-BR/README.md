# Documentação Auth SaaS

> Idioma: **PT-BR** | [EN-US](../en-US/README.md)

Plataforma de autenticação multi-tenant (`com.auth.saas`).

## Conteúdo

- [ADR-001 — Stack e Arquitetura](adr/ADR-001-stack-and-architecture.md)

## Módulos

| Módulo | Papel |
|---|---|
| `auth-domain` | Contratos e modelos de domínio |
| `auth-persistence` | Persistência platform/tenant e Flyway |
| `auth-control-plane` | API de onboarding e admin de tenant |
| `auth-data-plane` | API de autenticação e tokens |
