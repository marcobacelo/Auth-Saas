package com.auth.saas.dataplane.api;

import com.auth.saas.dataplane.auth.ApiKeyAuthenticationProvider;
import com.auth.saas.dataplane.auth.PasswordAuthenticationProvider;
import com.auth.saas.dataplane.tenant.TenantContexts;
import com.auth.saas.dataplane.token.TokenService;
import com.auth.saas.domain.auth.AuthMethod;
import com.auth.saas.domain.auth.AuthRequest;
import com.auth.saas.domain.auth.AuthResult;
import com.auth.saas.domain.common.DomainException;
import com.auth.saas.domain.tenant.TenantContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/t/{tenantSlug}/v1/auth")
public class AuthController {

    private final PasswordAuthenticationProvider passwordAuthenticationProvider;
    private final ApiKeyAuthenticationProvider apiKeyAuthenticationProvider;
    private final TokenService tokenService;

    public AuthController(
            PasswordAuthenticationProvider passwordAuthenticationProvider,
            ApiKeyAuthenticationProvider apiKeyAuthenticationProvider,
            TokenService tokenService) {
        this.passwordAuthenticationProvider = passwordAuthenticationProvider;
        this.apiKeyAuthenticationProvider = apiKeyAuthenticationProvider;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public TokenResponse login(
            @PathVariable String tenantSlug,
            @Valid @RequestBody LoginRequest body,
            HttpServletRequest request) {
        TenantContext tenantContext = TenantContexts.require(request);
        AuthResult result = passwordAuthenticationProvider.authenticate(new AuthRequest(
                tenantContext,
                AuthMethod.PASSWORD,
                Map.of("username", body.username(), "password", body.password())));
        return toResponse(tokenService.issue(tenantContext, result));
    }

    @PostMapping("/api-key")
    public TokenResponse apiKeyLogin(
            @PathVariable String tenantSlug,
            @Valid @RequestBody ApiKeyRequest body,
            HttpServletRequest request) {
        TenantContext tenantContext = TenantContexts.require(request);
        AuthResult result = apiKeyAuthenticationProvider.authenticate(new AuthRequest(
                tenantContext,
                AuthMethod.API_KEY,
                Map.of("apiKey", body.apiKey())));
        return toResponse(tokenService.issue(tenantContext, result));
    }

    @PostMapping("/token/refresh")
    public TokenResponse refresh(
            @PathVariable String tenantSlug,
            @Valid @RequestBody RefreshRequest body,
            HttpServletRequest request) {
        TenantContext tenantContext = TenantContexts.require(request);
        var principal = tokenService.consumeRefresh(tenantSlug, body.refreshToken())
                .orElseThrow(() -> new DomainException("INVALID_REFRESH", "refresh token is invalid or expired"));

        AuthResult result = new AuthResult(
                principal.subjectId(),
                principal.username(),
                principal.roles().isEmpty() ? Set.of("USER") : principal.roles(),
                AuthMethod.REFRESH_TOKEN,
                false);
        return toResponse(tokenService.issue(tenantContext, result));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @PathVariable String tenantSlug,
            @Valid @RequestBody RefreshRequest body) {
        tokenService.revokeRefresh(tenantSlug, body.refreshToken());
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Map<String, String>> handleDomain(DomainException ex) {
        HttpStatus status = switch (ex.code()) {
            case "INVALID_CREDENTIALS", "INVALID_REFRESH" -> HttpStatus.UNAUTHORIZED;
            case "PROVIDER_DISABLED" -> HttpStatus.FORBIDDEN;
            default -> HttpStatus.BAD_REQUEST;
        };
        return ResponseEntity.status(status).body(Map.of("code", ex.code(), "message", ex.getMessage()));
    }

    private static TokenResponse toResponse(TokenService.TokenPair pair) {
        return new TokenResponse(
                pair.accessToken(),
                pair.expiresIn(),
                pair.refreshToken(),
                pair.refreshExpiresIn(),
                "Bearer");
    }

    public record LoginRequest(@NotBlank String username, @NotBlank String password) {
    }

    public record ApiKeyRequest(@NotBlank String apiKey) {
    }

    public record RefreshRequest(@NotBlank String refreshToken) {
    }

    public record TokenResponse(
            String accessToken,
            long expiresIn,
            String refreshToken,
            long refreshExpiresIn,
            String tokenType) {
    }
}
