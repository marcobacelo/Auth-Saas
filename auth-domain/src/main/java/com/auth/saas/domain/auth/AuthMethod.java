package com.auth.saas.domain.auth;

public enum AuthMethod {
    PASSWORD,
    OIDC,
    API_KEY,
    MFA_TOTP,
    REFRESH_TOKEN
}
