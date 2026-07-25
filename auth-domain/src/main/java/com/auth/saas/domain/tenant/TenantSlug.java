package com.auth.saas.domain.tenant;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public record TenantSlug(String value) {

    private static final Pattern SLUG_PATTERN = Pattern.compile("^[a-z0-9]([a-z0-9-]{1,61}[a-z0-9])?$");

    public TenantSlug {
        Objects.requireNonNull(value, "tenant slug must not be null");
        String normalized = value.trim().toLowerCase(Locale.ROOT);
        if (!SLUG_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException(
                    "tenant slug must be 3-63 chars, lowercase alphanumeric/hyphen, and not start/end with hyphen");
        }
        value = normalized;
    }

    public String schemaName() {
        return "t_" + value.replace('-', '_');
    }
}
