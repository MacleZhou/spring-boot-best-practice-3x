package org.springframework.mobile.device.switcher;

import jakarta.servlet.http.HttpServletRequest;

public interface SiteUrlFactory {
    boolean isRequestForSite(HttpServletRequest var1);

    String createSiteUrl(HttpServletRequest var1);
}
