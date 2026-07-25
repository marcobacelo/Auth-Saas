package com.auth.saas.controlplane.service;

import com.auth.saas.controlplane.api.OnboardTenantRequest;
import com.auth.saas.controlplane.api.OnboardTenantResponse;
import com.auth.saas.domain.common.DomainException;
import com.auth.saas.domain.crypto.PasswordHasher;
import com.auth.saas.domain.tenant.Tenant;
import com.auth.saas.domain.tenant.TenantId;
import com.auth.saas.domain.tenant.TenantProvisioningPort;
import com.auth.saas.domain.tenant.TenantRepository;
import com.auth.saas.domain.tenant.TenantSlug;
import com.auth.saas.domain.tenant.TenantStatus;
import com.auth.saas.persistence.tenant.TenantIdentityJdbcRepository;
import com.auth.saas.persistence.tenant.TenantSchemaContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class TenantOnboardingService {

    private final TenantRepository tenantRepository;
    private final TenantProvisioningPort provisioningPort;
    private final TenantIdentityJdbcRepository identityJdbcRepository;
    private final PasswordHasher passwordHasher;

    public TenantOnboardingService(
            TenantRepository tenantRepository,
            TenantProvisioningPort provisioningPort,
            TenantIdentityJdbcRepository identityJdbcRepository,
            PasswordHasher passwordHasher) {
        this.tenantRepository = tenantRepository;
        this.provisioningPort = provisioningPort;
        this.identityJdbcRepository = identityJdbcRepository;
        this.passwordHasher = passwordHasher;
    }

    @Transactional
    public OnboardTenantResponse onboard(OnboardTenantRequest request) {
        TenantSlug slug = new TenantSlug(request.slug());
        if (tenantRepository.existsBySlug(slug)) {
            throw new DomainException("TENANT_EXISTS", "tenant slug already exists: " + slug.value());
        }

        Instant now = Instant.now();
        Tenant tenant = new Tenant(
                TenantId.random(),
                slug,
                request.displayName().trim(),
                TenantStatus.PROVISIONING,
                slug.schemaName(),
                now,
                now);

        tenantRepository.save(tenant);

        try {
            provisioningPort.provisionSchema(tenant);

            TenantSchemaContextHolder.set(tenant.schemaName());
            try {
                String passwordHash = passwordHasher.hash(request.adminPassword().toCharArray());
                identityJdbcRepository.createAdminIdentity(
                        UUID.randomUUID(),
                        request.adminUsername().trim(),
                        passwordHash,
                        now);
            } finally {
                TenantSchemaContextHolder.clear();
            }

            Tenant active = tenant.withStatus(TenantStatus.ACTIVE, Instant.now());
            tenantRepository.save(active);

            return new OnboardTenantResponse(
                    active.id().value(),
                    active.slug().value(),
                    active.schemaName(),
                    active.status().name(),
                    request.adminUsername().trim(),
                    "/t/" + active.slug().value() + "/v1/auth");
        } catch (RuntimeException ex) {
            tenantRepository.save(tenant.withStatus(TenantStatus.FAILED, Instant.now()));
            throw new DomainException("TENANT_PROVISIONING_FAILED", "failed to provision tenant: " + ex.getMessage());
        }
    }
}
