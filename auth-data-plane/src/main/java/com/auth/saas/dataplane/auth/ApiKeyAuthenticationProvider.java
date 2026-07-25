package com.auth.saas.dataplane.auth;

import com.auth.saas.domain.auth.AuthMethod;
import com.auth.saas.domain.auth.AuthRequest;
import com.auth.saas.domain.auth.AuthResult;
import com.auth.saas.domain.auth.AuthenticationProvider;
import com.auth.saas.domain.common.DomainException;
import com.auth.saas.domain.crypto.PasswordHasher;
import com.auth.saas.domain.tenant.TenantContext;
import com.auth.saas.persistence.tenant.TenantIdentityJdbcRepository;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyAuthenticationProvider implements AuthenticationProvider {

    private final TenantIdentityJdbcRepository identityRepository;
    private final PasswordHasher passwordHasher;

    public ApiKeyAuthenticationProvider(
            TenantIdentityJdbcRepository identityRepository,
            PasswordHasher passwordHasher) {
        this.identityRepository = identityRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public AuthMethod method() {
        return AuthMethod.API_KEY;
    }

    @Override
    public boolean supports(TenantContext tenantContext) {
        return identityRepository.isProviderEnabled(AuthMethod.API_KEY.name());
    }

    @Override
    public AuthResult authenticate(AuthRequest request) {
        if (!supports(request.tenantContext())) {
            throw new DomainException("PROVIDER_DISABLED", "api key authentication is disabled for tenant");
        }

        String apiKey = request.require("apiKey");
        if (apiKey.length() < 12) {
            throw new DomainException("INVALID_CREDENTIALS", "invalid api key");
        }

        String prefix = apiKey.substring(0, 8);
        var record = identityRepository.findApiKeyByPrefix(prefix)
                .orElseThrow(() -> new DomainException("INVALID_CREDENTIALS", "invalid api key"));

        if (!passwordHasher.matches(apiKey.toCharArray(), record.keyHash())) {
            throw new DomainException("INVALID_CREDENTIALS", "invalid api key");
        }

        return new AuthResult(
                record.identityId(),
                "api-key:" + record.prefix(),
                identityRepository.findRoles(record.identityId()),
                AuthMethod.API_KEY,
                false);
    }
}
