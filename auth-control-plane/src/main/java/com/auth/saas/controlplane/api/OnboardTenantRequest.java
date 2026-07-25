package com.auth.saas.controlplane.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OnboardTenantRequest(
        @NotBlank @Size(min = 3, max = 63) String slug,
        @NotBlank @Size(max = 255) String displayName,
        @NotBlank @Size(min = 3, max = 255) String adminUsername,
        @NotBlank @Size(min = 12, max = 128) String adminPassword) {
}
