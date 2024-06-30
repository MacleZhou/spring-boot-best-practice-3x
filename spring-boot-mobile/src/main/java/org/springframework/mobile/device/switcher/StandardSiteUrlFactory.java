package org.springframework.mobile.device.switcher;

import jakarta.servlet.http.HttpServletRequest;

public class StandardSiteUrlFactory extends AbstractSiteUrlFactory implements SiteUrlFactory {
    private final String serverName;

    public StandardSiteUrlFactory(String serverName) {
        this.serverName = serverName;
    }

    public boolean isRequestForSite(HttpServletRequest request) {
        return this.serverName.equals(request.getServerName());
    }

    public String createSiteUrl(HttpServletRequest request) {
        return this.createSiteUrlInternal(request, this.serverName, request.getRequestURI());
    }
}
