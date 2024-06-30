package org.springframework.mobile.device.site;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface SitePreferenceRepository {
    SitePreference loadSitePreference(HttpServletRequest var1);

    void saveSitePreference(SitePreference var1, HttpServletRequest var2, HttpServletResponse var3);
}
