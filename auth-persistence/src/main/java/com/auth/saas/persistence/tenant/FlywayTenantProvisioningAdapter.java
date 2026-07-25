package com.auth.saas.persistence.tenant;

import com.auth.saas.domain.common.DomainException;
import com.auth.saas.domain.tenant.Tenant;
import com.auth.saas.domain.tenant.TenantProvisioningPort;
import org.flywaydb.core.Flyway;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class FlywayTenantProvisioningAdapter implements TenantProvisioningPort {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    public FlywayTenantProvisioningAdapter(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void provisionSchema(Tenant tenant) {
        String schema = tenant.schemaName();
        validateSchemaName(schema);

        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS \"" + schema + "\"");

        Flyway.configure()
                .dataSource(dataSource)
                .schemas(schema)
                .defaultSchema(schema)
                .locations("classpath:db/tenant")
                .baselineOnMigrate(true)
                .load()
                .migrate();
    }

    @Override
    public void dropSchema(Tenant tenant) {
        String schema = tenant.schemaName();
        validateSchemaName(schema);
        jdbcTemplate.execute("DROP SCHEMA IF EXISTS \"" + schema + "\" CASCADE");
    }

    private static void validateSchemaName(String schema) {
        if (schema == null || !schema.matches("^t_[a-z0-9_]+$")) {
            throw new DomainException("INVALID_SCHEMA", "refusing to manage schema: " + schema);
        }
    }
}
