package com.auth.saas.domain.tenant;

import java.time.Instant;
import java.util.Objects;

public final class Tenant {

    private final TenantId id;
    private final TenantSlug slug;
    private final String displayName;
    private final TenantStatus status;
    private final String schemaName;
    private final Instant createdAt;
    private final Instant updatedAt;

    public Tenant(
            TenantId id,
            TenantSlug slug,
            String displayName,
            TenantStatus status,
            String schemaName,
            Instant createdAt,
            Instant updatedAt) {
        this.id = Objects.requireNonNull(id);
        this.slug = Objects.requireNonNull(slug);
        this.displayName = Objects.requireNonNull(displayName);
        this.status = Objects.requireNonNull(status);
        this.schemaName = Objects.requireNonNull(schemaName);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
    }

    public TenantId id() {
        return id;
    }

    public TenantSlug slug() {
        return slug;
    }

    public String displayName() {
        return displayName;
    }

    public TenantStatus status() {
        return status;
    }

    public String schemaName() {
        return schemaName;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    public Tenant withStatus(TenantStatus newStatus, Instant at) {
        return new Tenant(id, slug, displayName, newStatus, schemaName, createdAt, at);
    }
}
