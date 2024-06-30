package org.springframework.mobile.device.site;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface SitePreferenceHandler {
    String CURRENT_SITE_PREFERENCE_ATTRIBUTE = "currentSitePreference";

    SitePreference handleSitePreference(HttpServletRequest var1, HttpServletResponse var2);
}
