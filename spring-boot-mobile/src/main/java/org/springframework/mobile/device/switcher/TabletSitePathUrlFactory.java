package org.springframework.mobile.device.switcher;

import jakarta.servlet.http.HttpServletRequest;

public class TabletSitePathUrlFactory extends AbstractSitePathUrlFactory implements SiteUrlFactory {
    public TabletSitePathUrlFactory(String tabletPath, String mobilePath) {
        this(tabletPath, mobilePath, (String)null);
    }

    public TabletSitePathUrlFactory(String tabletPath, String mobilePath, String rootPath) {
        super(mobilePath, tabletPath, rootPath);
    }

    public boolean isRequestForSite(HttpServletRequest request) {
        return request.getRequestURI().startsWith(this.getFullTabletPath()) || request.getRequestURI().equals(this.getCleanTabletPath());
    }

    public String createSiteUrl(HttpServletRequest request) {
        String urlPath = request.getRequestURI();
        if (this.getCleanMobilePath() != null && urlPath.startsWith(this.getCleanMobilePath())) {
            urlPath = urlPath.substring(this.getCleanMobilePath().length());
        } else if (this.getRootPath() != null && urlPath.startsWith(this.getCleanRootPath())) {
            urlPath = urlPath.substring(this.getCleanRootPath().length());
        }

        urlPath = this.getCleanTabletPath() + urlPath;
        return this.createSiteUrlInternal(request, request.getServerName(), urlPath);
    }
}
