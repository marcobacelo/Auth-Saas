package com.auth.saas.persistence.tenant;

public final class TenantSchemaContextHolder {

    private static final ThreadLocal<String> CURRENT_SCHEMA = new ThreadLocal<>();

    private TenantSchemaContextHolder() {
    }

    public static void set(String schemaName) {
        CURRENT_SCHEMA.set(schemaName);
    }

    public static String get() {
        return CURRENT_SCHEMA.get();
    }

    public static void clear() {
        CURRENT_SCHEMA.remove();
    }
}
