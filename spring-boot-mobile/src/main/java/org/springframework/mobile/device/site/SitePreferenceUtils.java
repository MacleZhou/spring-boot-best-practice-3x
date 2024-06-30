package org.springframework.mobile.device.site;

import org.springframework.web.context.request.RequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

public class SitePreferenceUtils {
    public static SitePreference getCurrentSitePreference(HttpServletRequest request) {
        return (SitePreference)request.getAttribute("currentSitePreference");
    }

    public static SitePreference getCurrentSitePreference(RequestAttributes attributes) {
        return (SitePreference)attributes.getAttribute("currentSitePreference", 0);
    }

    private SitePreferenceUtils() {
    }
}
