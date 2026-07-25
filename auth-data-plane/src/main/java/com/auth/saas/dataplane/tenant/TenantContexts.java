package com.auth.saas.dataplane.tenant;

import com.auth.saas.domain.tenant.TenantContext;
import jakarta.servlet.http.HttpServletRequest;

public final class TenantContexts {

    private TenantContexts() {
    }

    public static TenantContext require(HttpServletRequest request) {
        Object value = request.getAttribute(TenantContext.class.getName());
        if (!(value instanceof TenantContext context)) {
            throw new IllegalStateException("tenant context missing from request");
        }
        return context;
    }
}
