package com.auth.saas.persistence.tenant;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Applies {@code SET search_path} for the current tenant schema on borrowed connections.
 */
public final class SchemaPerTenantConnectionInitializer {

    private SchemaPerTenantConnectionInitializer() {
    }

    public static Connection initialize(Connection connection) throws SQLException {
        String schema = TenantSchemaContextHolder.get();
        if (schema != null && !schema.isBlank()) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("SET search_path TO \"" + schema.replace("\"", "") + "\", public");
            }
        }
        return connection;
    }
}
