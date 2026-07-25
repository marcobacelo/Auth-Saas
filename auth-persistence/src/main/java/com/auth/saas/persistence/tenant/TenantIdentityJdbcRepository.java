package com.auth.saas.persistence.tenant;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public class TenantIdentityJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public TenantIdentityJdbcRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Optional<IdentityRecord> findByUsername(String username) {
        List<IdentityRecord> rows = jdbcTemplate.query(
                """
                SELECT i.identity_id, i.username, i.enabled, c.secret_hash
                FROM identities i
                LEFT JOIN credentials c ON c.identity_id = i.identity_id AND c.method = 'PASSWORD'
                WHERE i.username = ?
                """,
                (rs, rowNum) -> new IdentityRecord(
                        rs.getObject("identity_id", UUID.class),
                        rs.getString("username"),
                        rs.getBoolean("enabled"),
                        rs.getString("secret_hash")),
                username);
        return rows.stream().findFirst();
    }

    public Set<String> findRoles(UUID identityId) {
        List<String> roles = jdbcTemplate.query(
                """
                SELECT r.name
                FROM roles r
                INNER JOIN identity_roles ir ON ir.role_id = r.role_id
                WHERE ir.identity_id = ?
                """,
                (rs, rowNum) -> rs.getString("name"),
                identityId);
        return new HashSet<>(roles);
    }

    public void createAdminIdentity(UUID identityId, String username, String passwordHash, Instant now) {
        jdbcTemplate.update(
                """
                INSERT INTO identities (identity_id, username, email, enabled, created_at, updated_at)
                VALUES (?, ?, NULL, TRUE, ?, ?)
                """,
                identityId, username, now, now);

        jdbcTemplate.update(
                """
                INSERT INTO credentials (credential_id, identity_id, method, secret_hash, created_at, updated_at)
                VALUES (?, ?, 'PASSWORD', ?, ?, ?)
                """,
                UUID.randomUUID(), identityId, passwordHash, now, now);

        jdbcTemplate.update(
                """
                INSERT INTO identity_roles (identity_id, role_id)
                VALUES (?, '11111111-1111-1111-1111-111111111111')
                """,
                identityId);
    }

    public Optional<ApiKeyRecord> findApiKeyByPrefix(String prefix) {
        List<ApiKeyRecord> rows = jdbcTemplate.query(
                """
                SELECT api_key_id, identity_id, key_prefix, key_hash, enabled
                FROM api_keys
                WHERE key_prefix = ? AND enabled = TRUE
                """,
                (rs, rowNum) -> new ApiKeyRecord(
                        rs.getObject("api_key_id", UUID.class),
                        rs.getObject("identity_id", UUID.class),
                        rs.getString("key_prefix"),
                        rs.getString("key_hash"),
                        rs.getBoolean("enabled")),
                prefix);
        return rows.stream().findFirst();
    }

    public boolean isProviderEnabled(String method) {
        Boolean enabled = jdbcTemplate.query(
                "SELECT enabled FROM auth_provider_configs WHERE method = ?",
                rs -> rs.next() ? rs.getBoolean("enabled") : Boolean.FALSE,
                method);
        return Boolean.TRUE.equals(enabled);
    }

    public record IdentityRecord(UUID identityId, String username, boolean enabled, String passwordHash) {
    }

    public record ApiKeyRecord(UUID apiKeyId, UUID identityId, String prefix, String keyHash, boolean enabled) {
    }
}
