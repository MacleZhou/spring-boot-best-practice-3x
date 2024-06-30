package org.springframework.mobile.device.switcher;

import jakarta.servlet.http.HttpServletRequest;

public class NormalSitePathUrlFactory extends AbstractSitePathUrlFactory implements SiteUrlFactory {
    public NormalSitePathUrlFactory(String mobilePath) {
        this(mobilePath, (String)null, (String)null);
    }

    public NormalSitePathUrlFactory(String mobilePath, String rootPath) {
        this(mobilePath, (String)null, rootPath);
    }

    public NormalSitePathUrlFactory(String mobilePath, String tabletPath, String rootPath) {
        super(mobilePath, tabletPath, rootPath);
    }

    public boolean isRequestForSite(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        if (!this.hasMobilePath() || !requestURI.startsWith(this.getFullMobilePath()) && !requestURI.equals(this.getCleanMobilePath())) {
            return !this.hasTabletPath() || !requestURI.startsWith(this.getFullTabletPath()) && !requestURI.equals(this.getCleanTabletPath());
        } else {
            return false;
        }
    }

    public String createSiteUrl(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String adjustedRequestURI = "";
        if ((!this.hasMobilePath() || !requestURI.equals(this.getCleanMobilePath())) && (!this.hasTabletPath() || !requestURI.equals(this.getCleanTabletPath()))) {
            if (this.hasMobilePath() && requestURI.startsWith(this.getFullMobilePath())) {
                if (this.hasRootPath()) {
                    adjustedRequestURI = this.getRootPath() + requestURI.substring(this.getFullMobilePath().length());
                } else {
                    adjustedRequestURI = requestURI.substring(this.getCleanMobilePath().length());
                }
            } else if (this.hasTabletPath() && requestURI.startsWith(this.getFullTabletPath())) {
                if (this.hasRootPath()) {
                    adjustedRequestURI = this.getRootPath() + requestURI.substring(this.getFullTabletPath().length());
                } else {
                    adjustedRequestURI = requestURI.substring(this.getCleanTabletPath().length());
                }
            }
        } else if (this.hasRootPath()) {
            adjustedRequestURI = this.getCleanRootPath();
        }

        return this.createSiteUrlInternal(request, request.getServerName(), adjustedRequestURI);
    }
}
