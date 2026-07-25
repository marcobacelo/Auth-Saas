package com.auth.saas.dataplane.tenant;

import com.auth.saas.domain.tenant.Tenant;
import com.auth.saas.domain.tenant.TenantContext;
import com.auth.saas.domain.tenant.TenantRepository;
import com.auth.saas.domain.tenant.TenantSlug;
import com.auth.saas.domain.tenant.TenantStatus;
import com.auth.saas.persistence.tenant.TenantSchemaContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 20)
public class TenantResolutionFilter extends OncePerRequestFilter {

    private static final Pattern TENANT_PATH = Pattern.compile("^/t/([a-z0-9-]+)(/.*)?$");

    private final TenantRepository tenantRepository;

    public TenantResolutionFilter(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/actuator");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        Matcher matcher = TENANT_PATH.matcher(request.getRequestURI());
        if (!matcher.matches()) {
            filterChain.doFilter(request, response);
            return;
        }

        String slugValue = matcher.group(1);
        Optional<Tenant> tenant = tenantRepository.findBySlug(new TenantSlug(slugValue));
        if (tenant.isEmpty() || tenant.get().status() != TenantStatus.ACTIVE) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getOutputStream()
                    .write("{\"code\":\"TENANT_NOT_FOUND\",\"message\":\"tenant not found or inactive\"}"
                            .getBytes(StandardCharsets.UTF_8));
            return;
        }

        TenantContext context = TenantContext.from(tenant.get());
        TenantSchemaContextHolder.set(context.schemaName());
        request.setAttribute(TenantContext.class.getName(), context);
        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantSchemaContextHolder.clear();
        }
    }
}
