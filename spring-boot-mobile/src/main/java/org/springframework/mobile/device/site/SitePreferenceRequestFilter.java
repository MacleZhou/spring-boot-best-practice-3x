package org.springframework.mobile.device.site;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SitePreferenceRequestFilter extends OncePerRequestFilter {
    private final SitePreferenceHandler sitePreferenceHandler;

    public SitePreferenceRequestFilter() {
        this(new StandardSitePreferenceHandler(new CookieSitePreferenceRepository()));
    }

    public SitePreferenceRequestFilter(SitePreferenceHandler sitePreferenceHandler) {
        this.sitePreferenceHandler = sitePreferenceHandler;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        this.sitePreferenceHandler.handleSitePreference(request, response);
        filterChain.doFilter(request, response);
    }
}
