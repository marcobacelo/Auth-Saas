package com.auth.saas.domain.auth;

import com.auth.saas.domain.tenant.TenantContext;

import java.util.Map;
import java.util.Objects;

public record AuthRequest(
        TenantContext tenantContext,
        AuthMethod method,
        Map<String, String> attributes) {

    public AuthRequest {
        Objects.requireNonNull(tenantContext, "tenantContext");
        Objects.requireNonNull(method, "method");
        attributes = Map.copyOf(Objects.requireNonNullElse(attributes, Map.of()));
    }

    public String require(String key) {
        String value = attributes.get(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("missing auth attribute: " + key);
        }
        return value;
    }
}
