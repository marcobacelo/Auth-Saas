package com.auth.saas.dataplane.api;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.interfaces.RSAPublicKey;
import java.util.Map;

@RestController
public class JwksController {

    private final RSAPublicKey publicKey;

    public JwksController(@Value("${auth.jwt.public-key}") RSAPublicKey publicKey) {
        this.publicKey = publicKey;
    }

    @GetMapping("/t/{tenantSlug}/v1/.well-known/jwks.json")
    public Map<String, Object> jwks(@PathVariable String tenantSlug) {
        RSAKey key = new RSAKey.Builder(publicKey).keyID("auth-saas-default").build();
        return new JWKSet(key).toJSONObject();
    }
}
