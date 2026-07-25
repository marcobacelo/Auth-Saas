package com.auth.saas.dataplane.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * OIDC provider wiring placeholder. Full authorization-code flow lands in the next iteration.
 */
@RestController
@RequestMapping("/t/{tenantSlug}/v1/auth/oauth2")
public class OidcStubController {

    @GetMapping("/{provider}/authorize")
    public ResponseEntity<Map<String, Object>> authorize(
            @PathVariable String tenantSlug,
            @PathVariable String provider) {
        return ResponseEntity.status(501).body(Map.of(
                "code", "OIDC_NOT_IMPLEMENTED",
                "message", "OIDC authorization for provider '" + provider + "' is scaffolded and not yet implemented",
                "tenant", tenantSlug));
    }
}
