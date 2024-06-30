package org.springframework.mobile.device.site;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SitePreferenceHandlerInterceptor implements HandlerInterceptor {
    private final SitePreferenceHandler sitePreferenceHandler;

    public SitePreferenceHandlerInterceptor() {
        this(new StandardSitePreferenceHandler(new CookieSitePreferenceRepository()));
    }

    public SitePreferenceHandlerInterceptor(SitePreferenceHandler sitePreferenceHandler) {
        this.sitePreferenceHandler = sitePreferenceHandler;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        this.sitePreferenceHandler.handleSitePreference(request, response);
        return true;
    }
}
