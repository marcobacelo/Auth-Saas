package com.auth.saas.controlplane.api;

import java.util.UUID;

public record OnboardTenantResponse(
        UUID tenantId,
        String slug,
        String schemaName,
        String status,
        String adminUsername,
        String loginBasePath) {
}
