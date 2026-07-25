package com.auth.saas.persistence.platform;

import com.auth.saas.domain.tenant.Tenant;
import com.auth.saas.domain.tenant.TenantId;
import com.auth.saas.domain.tenant.TenantRepository;
import com.auth.saas.domain.tenant.TenantSlug;
import com.auth.saas.domain.tenant.TenantStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class JpaTenantRepository implements TenantRepository {

    private final JpaTenantEntityRepository entityRepository;

    public JpaTenantRepository(JpaTenantEntityRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    @Transactional
    public Tenant save(Tenant tenant) {
        TenantEntity entity = entityRepository.findById(tenant.id().value())
                .orElseGet(() -> new TenantEntity(
                        tenant.id().value(),
                        tenant.slug().value(),
                        tenant.displayName(),
                        tenant.status(),
                        tenant.schemaName(),
                        tenant.createdAt(),
                        tenant.updatedAt()));

        entity.setStatus(tenant.status());
        entity.setUpdatedAt(tenant.updatedAt());
        return toDomain(entityRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tenant> findById(TenantId id) {
        return entityRepository.findById(id.value()).map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tenant> findBySlug(TenantSlug slug) {
        return entityRepository.findBySlug(slug.value()).map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsBySlug(TenantSlug slug) {
        return entityRepository.existsBySlug(slug.value());
    }

    private Tenant toDomain(TenantEntity entity) {
        return new Tenant(
                TenantId.of(entity.getTenantId()),
                new TenantSlug(entity.getSlug()),
                entity.getDisplayName(),
                entity.getStatus() == null ? TenantStatus.FAILED : entity.getStatus(),
                entity.getSchemaName(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
