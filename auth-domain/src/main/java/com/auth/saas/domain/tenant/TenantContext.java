package com.auth.saas.domain.tenant;

import java.util.Objects;

public record TenantContext(TenantId tenantId, TenantSlug slug, String schemaName) {

    public TenantContext {
        Objects.requireNonNull(tenantId, "tenantId");
        Objects.requireNonNull(slug, "slug");
        Objects.requireNonNull(schemaName, "schemaName");
    }

    public static TenantContext from(Tenant tenant) {
        return new TenantContext(tenant.id(), tenant.slug(), tenant.schemaName());
    }
}
