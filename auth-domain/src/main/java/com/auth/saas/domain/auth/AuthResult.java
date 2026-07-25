package com.auth.saas.domain.auth;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public record AuthResult(
        UUID subjectId,
        String username,
        Set<String> roles,
        AuthMethod method,
        boolean mfaRequired) {

    public AuthResult {
        Objects.requireNonNull(subjectId, "subjectId");
        Objects.requireNonNull(username, "username");
        roles = Set.copyOf(Objects.requireNonNullElse(roles, Set.of()));
        Objects.requireNonNull(method, "method");
    }
}
