package com.auth.saas.domain.tenant;

public interface TenantProvisioningPort {

    void provisionSchema(Tenant tenant);

    void dropSchema(Tenant tenant);
}
