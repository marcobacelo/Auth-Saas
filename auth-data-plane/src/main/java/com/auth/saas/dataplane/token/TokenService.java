package com.auth.saas.dataplane.token;

import com.auth.saas.domain.auth.AuthResult;
import com.auth.saas.domain.tenant.TenantContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;
    private final StringRedisTemplate redisTemplate;
    private final String issuer;
    private final long accessTokenTtlSeconds;
    private final long refreshTokenTtlSeconds;
    private final SecureRandom secureRandom = new SecureRandom();

    public TokenService(
            JwtEncoder jwtEncoder,
            StringRedisTemplate redisTemplate,
            @Value("${auth.jwt.issuer:https://auth.saas.local}") String issuer,
            @Value("${auth.jwt.access-token-ttl-seconds:900}") long accessTokenTtlSeconds,
            @Value("${auth.jwt.refresh-token-ttl-seconds:2592000}") long refreshTokenTtlSeconds) {
        this.jwtEncoder = jwtEncoder;
        this.redisTemplate = redisTemplate;
        this.issuer = issuer;
        this.accessTokenTtlSeconds = accessTokenTtlSeconds;
        this.refreshTokenTtlSeconds = refreshTokenTtlSeconds;
    }

    public TokenPair issue(TenantContext tenantContext, AuthResult authResult) {
        Instant now = Instant.now();
        String scope = authResult.roles().stream().sorted().collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .subject(authResult.subjectId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(accessTokenTtlSeconds))
                .claim("tid", tenantContext.tenantId().value().toString())
                .claim("tenant", tenantContext.slug().value())
                .claim("username", authResult.username())
                .claim("scope", scope)
                .claim("amr", authResult.method().name().toLowerCase())
                .build();

        String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        String refreshToken = randomToken();

        String refreshKey = refreshKey(tenantContext.slug().value(), refreshToken);
        String refreshValue = authResult.subjectId() + "|" + authResult.username() + "|"
                + String.join(",", authResult.roles());
        redisTemplate.opsForValue().set(refreshKey, refreshValue, Duration.ofSeconds(refreshTokenTtlSeconds));

        return new TokenPair(accessToken, accessTokenTtlSeconds, refreshToken, refreshTokenTtlSeconds);
    }

    public Optional<RefreshPrincipal> consumeRefresh(String tenantSlug, String refreshToken) {
        String key = refreshKey(tenantSlug, refreshToken);
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return Optional.empty();
        }
        redisTemplate.delete(key);
        String[] parts = value.split("\\|", 3);
        return Optional.of(new RefreshPrincipal(
                UUID.fromString(parts[0]),
                parts[1],
                parts.length > 2 && !parts[2].isBlank()
                        ? java.util.Set.of(parts[2].split(","))
                        : java.util.Set.of()));
    }

    public void revokeRefresh(String tenantSlug, String refreshToken) {
        redisTemplate.delete(refreshKey(tenantSlug, refreshToken));
    }

    private String refreshKey(String tenantSlug, String refreshToken) {
        return "refresh:" + tenantSlug + ":" + refreshToken;
    }

    private String randomToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public record TokenPair(String accessToken, long expiresIn, String refreshToken, long refreshExpiresIn) {
    }

    public record RefreshPrincipal(UUID subjectId, String username, java.util.Set<String> roles) {
    }
}
