package com.auth.saas.domain.tenant;

import java.util.Optional;

public interface TenantRepository {

    Tenant save(Tenant tenant);

    Optional<Tenant> findById(TenantId id);

    Optional<Tenant> findBySlug(TenantSlug slug);

    boolean existsBySlug(TenantSlug slug);
}
