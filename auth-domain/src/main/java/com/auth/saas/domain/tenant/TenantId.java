package com.auth.saas.domain.tenant;

import java.util.Objects;
import java.util.UUID;

public record TenantId(UUID value) {

    public TenantId {
        Objects.requireNonNull(value, "tenant id must not be null");
    }

    public static TenantId of(UUID value) {
        return new TenantId(value);
    }

    public static TenantId random() {
        return new TenantId(UUID.randomUUID());
    }
}
