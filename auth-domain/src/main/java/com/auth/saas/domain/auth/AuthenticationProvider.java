package com.auth.saas.domain.auth;

import com.auth.saas.domain.tenant.TenantContext;

public interface AuthenticationProvider {

    AuthMethod method();

    boolean supports(TenantContext tenantContext);

    AuthResult authenticate(AuthRequest request);
}
