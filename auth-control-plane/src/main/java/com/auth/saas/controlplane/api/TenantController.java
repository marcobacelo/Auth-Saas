package com.auth.saas.controlplane.api;

import com.auth.saas.controlplane.service.TenantOnboardingService;
import com.auth.saas.domain.common.DomainException;
import com.auth.saas.domain.tenant.TenantRepository;
import com.auth.saas.domain.tenant.TenantSlug;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/platform/v1/tenants")
public class TenantController {

    private final TenantOnboardingService onboardingService;
    private final TenantRepository tenantRepository;

    public TenantController(TenantOnboardingService onboardingService, TenantRepository tenantRepository) {
        this.onboardingService = onboardingService;
        this.tenantRepository = tenantRepository;
    }

    @PostMapping
    public ResponseEntity<OnboardTenantResponse> onboard(@Valid @RequestBody OnboardTenantRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(onboardingService.onboard(request));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<?> getBySlug(@PathVariable String slug) {
        return tenantRepository.findBySlug(new TenantSlug(slug))
                .<ResponseEntity<?>>map(tenant -> ResponseEntity.ok(Map.of(
                        "tenantId", tenant.id().value(),
                        "slug", tenant.slug().value(),
                        "displayName", tenant.displayName(),
                        "status", tenant.status().name(),
                        "schemaName", tenant.schemaName())))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Map<String, String>> handleDomain(DomainException ex) {
        HttpStatus status = "TENANT_EXISTS".equals(ex.code()) ? HttpStatus.CONFLICT : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(Map.of("code", ex.code(), "message", ex.getMessage()));
    }
}
