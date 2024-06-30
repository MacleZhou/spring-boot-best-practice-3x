package org.springframework.mobile.device.switcher;

import jakarta.servlet.http.HttpServletRequest;

public class MobileSitePathUrlFactory extends AbstractSitePathUrlFactory implements SiteUrlFactory {
    public MobileSitePathUrlFactory(String mobilePath, String tabletPath) {
        this(mobilePath, tabletPath, (String)null);
    }

    public MobileSitePathUrlFactory(String mobilePath, String tabletPath, String rootPath) {
        super(mobilePath, tabletPath, rootPath);
    }

    public boolean isRequestForSite(HttpServletRequest request) {
        return request.getRequestURI().startsWith(this.getFullMobilePath()) || request.getRequestURI().equals(this.getCleanMobilePath());
    }

    public String createSiteUrl(HttpServletRequest request) {
        String urlPath = request.getRequestURI();
        if (this.getCleanTabletPath() != null && urlPath.startsWith(this.getCleanTabletPath())) {
            urlPath = urlPath.substring(this.getCleanTabletPath().length());
        } else if (this.getRootPath() != null && urlPath.startsWith(this.getCleanRootPath())) {
            urlPath = urlPath.substring(this.getCleanRootPath().length());
        }

        urlPath = this.getCleanMobilePath() + urlPath;
        return this.createSiteUrlInternal(request, request.getServerName(), urlPath);
    }
}
