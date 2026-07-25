CREATE TABLE IF NOT EXISTS platform_tenants (
    tenant_id      UUID PRIMARY KEY,
    slug           VARCHAR(63)  NOT NULL UNIQUE,
    display_name   VARCHAR(255) NOT NULL,
    status         VARCHAR(32)  NOT NULL,
    schema_name    VARCHAR(63)  NOT NULL UNIQUE,
    created_at     TIMESTAMPTZ  NOT NULL,
    updated_at     TIMESTAMPTZ  NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_platform_tenants_status ON platform_tenants (status);
