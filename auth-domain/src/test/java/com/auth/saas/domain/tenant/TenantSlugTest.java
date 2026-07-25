package com.auth.saas.domain.tenant;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TenantSlugTest {

    @Test
    void normalizesAndBuildsSchemaName() {
        TenantSlug slug = new TenantSlug("Acme-Corp");
        assertEquals("acme-corp", slug.value());
        assertEquals("t_acme_corp", slug.schemaName());
    }

    @Test
    void rejectsInvalidSlug() {
        assertThrows(IllegalArgumentException.class, () -> new TenantSlug("-bad"));
    }
}
