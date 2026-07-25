package com.auth.saas.dataplane.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * MFA TOTP verification placeholder for the v1 provider surface.
 */
@RestController
@RequestMapping("/t/{tenantSlug}/v1/auth/mfa")
public class MfaTotpStubController {

    @PostMapping("/totp/verify")
    public ResponseEntity<Map<String, Object>> verify(@PathVariable String tenantSlug) {
        return ResponseEntity.status(501).body(Map.of(
                "code", "MFA_TOTP_NOT_IMPLEMENTED",
                "message", "MFA TOTP verification is scaffolded and not yet implemented",
                "tenant", tenantSlug));
    }
}
