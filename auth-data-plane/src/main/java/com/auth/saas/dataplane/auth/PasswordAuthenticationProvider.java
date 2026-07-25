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
public class PasswordAuthenticationProvider implements AuthenticationProvider {

    private final TenantIdentityJdbcRepository identityRepository;
    private final PasswordHasher passwordHasher;

    public PasswordAuthenticationProvider(
            TenantIdentityJdbcRepository identityRepository,
            PasswordHasher passwordHasher) {
        this.identityRepository = identityRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public AuthMethod method() {
        return AuthMethod.PASSWORD;
    }

    @Override
    public boolean supports(TenantContext tenantContext) {
        return identityRepository.isProviderEnabled(AuthMethod.PASSWORD.name());
    }

    @Override
    public AuthResult authenticate(AuthRequest request) {
        if (!supports(request.tenantContext())) {
            throw new DomainException("PROVIDER_DISABLED", "password authentication is disabled for tenant");
        }

        String username = request.require("username");
        String password = request.require("password");

        var identity = identityRepository.findByUsername(username)
                .orElseThrow(() -> new DomainException("INVALID_CREDENTIALS", "invalid username or password"));

        if (!identity.enabled()
                || identity.passwordHash() == null
                || !passwordHasher.matches(password.toCharArray(), identity.passwordHash())) {
            throw new DomainException("INVALID_CREDENTIALS", "invalid username or password");
        }

        return new AuthResult(
                identity.identityId(),
                identity.username(),
                identityRepository.findRoles(identity.identityId()),
                AuthMethod.PASSWORD,
                false);
    }
}
