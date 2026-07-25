CREATE TABLE IF NOT EXISTS identities (
    identity_id    UUID PRIMARY KEY,
    username       VARCHAR(255) NOT NULL UNIQUE,
    email          VARCHAR(320),
    enabled        BOOLEAN NOT NULL DEFAULT TRUE,
    created_at     TIMESTAMPTZ NOT NULL,
    updated_at     TIMESTAMPTZ NOT NULL
);

CREATE TABLE IF NOT EXISTS roles (
    role_id        UUID PRIMARY KEY,
    name           VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS identity_roles (
    identity_id    UUID NOT NULL REFERENCES identities (identity_id) ON DELETE CASCADE,
    role_id        UUID NOT NULL REFERENCES roles (role_id) ON DELETE CASCADE,
    PRIMARY KEY (identity_id, role_id)
);

CREATE TABLE IF NOT EXISTS credentials (
    credential_id  UUID PRIMARY KEY,
    identity_id    UUID NOT NULL REFERENCES identities (identity_id) ON DELETE CASCADE,
    method         VARCHAR(32) NOT NULL,
    secret_hash    TEXT,
    external_subject VARCHAR(512),
    metadata_json  TEXT,
    created_at     TIMESTAMPTZ NOT NULL,
    updated_at     TIMESTAMPTZ NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_credentials_identity ON credentials (identity_id);
CREATE INDEX IF NOT EXISTS idx_credentials_method ON credentials (method);

CREATE TABLE IF NOT EXISTS auth_provider_configs (
    provider_id    UUID PRIMARY KEY,
    method         VARCHAR(32) NOT NULL,
    enabled        BOOLEAN NOT NULL DEFAULT TRUE,
    config_json    TEXT NOT NULL DEFAULT '{}',
    created_at     TIMESTAMPTZ NOT NULL,
    updated_at     TIMESTAMPTZ NOT NULL,
    UNIQUE (method)
);

CREATE TABLE IF NOT EXISTS api_keys (
    api_key_id     UUID PRIMARY KEY,
    identity_id    UUID NOT NULL REFERENCES identities (identity_id) ON DELETE CASCADE,
    key_prefix     VARCHAR(16) NOT NULL,
    key_hash       TEXT NOT NULL,
    name           VARCHAR(128) NOT NULL,
    enabled        BOOLEAN NOT NULL DEFAULT TRUE,
    created_at     TIMESTAMPTZ NOT NULL,
    last_used_at   TIMESTAMPTZ
);

CREATE TABLE IF NOT EXISTS mfa_totp_secrets (
    identity_id    UUID PRIMARY KEY REFERENCES identities (identity_id) ON DELETE CASCADE,
    secret_encrypted TEXT NOT NULL,
    enabled        BOOLEAN NOT NULL DEFAULT FALSE,
    created_at     TIMESTAMPTZ NOT NULL
);

INSERT INTO roles (role_id, name)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'ADMIN'),
    ('22222222-2222-2222-2222-222222222222', 'USER')
ON CONFLICT DO NOTHING;

INSERT INTO auth_provider_configs (provider_id, method, enabled, config_json, created_at, updated_at)
VALUES
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'PASSWORD', TRUE, '{}', NOW(), NOW()),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'OIDC', FALSE, '{}', NOW(), NOW()),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'API_KEY', TRUE, '{}', NOW(), NOW()),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'MFA_TOTP', FALSE, '{}', NOW(), NOW())
ON CONFLICT DO NOTHING;
