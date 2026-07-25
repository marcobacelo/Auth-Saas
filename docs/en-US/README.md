# Auth SaaS Documentation

> Language: **EN-US** | [PT-BR](../pt-BR/README.md)

Multi-tenant authentication platform (`com.auth.saas`).

## Contents

- [ADR-001 — Stack and Architecture](adr/ADR-001-stack-and-architecture.md)
- [Roadmap](ROADMAP.md) — resume work in a new local session here

## Modules

| Module | Role |
|---|---|
| `auth-domain` | Domain contracts and models |
| `auth-persistence` | Platform/tenant persistence and Flyway |
| `auth-control-plane` | Tenant onboarding and admin API |
| `auth-data-plane` | Authentication and token API |
