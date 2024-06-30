package org.springframework.mobile.device.switcher;

import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mobile.device.site.SitePreferenceHandler;
import org.springframework.mobile.device.util.ResolverUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StandardSiteSwitcherHandler implements SiteSwitcherHandler {
    private final SiteUrlFactory normalSiteUrlFactory;
    private final SiteUrlFactory mobileSiteUrlFactory;
    private final SiteUrlFactory tabletSiteUrlFactory;
    private final SitePreferenceHandler sitePreferenceHandler;
    private final boolean tabletIsMobile;

    public StandardSiteSwitcherHandler(SiteUrlFactory normalSiteUrlFactory, SiteUrlFactory mobileSiteUrlFactory, SiteUrlFactory tabletSiteUrlFactory, SitePreferenceHandler sitePreferenceHandler, Boolean tabletIsMobile) {
        this.normalSiteUrlFactory = normalSiteUrlFactory;
        this.mobileSiteUrlFactory = mobileSiteUrlFactory;
        this.tabletSiteUrlFactory = tabletSiteUrlFactory;
        this.sitePreferenceHandler = sitePreferenceHandler;
        this.tabletIsMobile = tabletIsMobile == null ? false : tabletIsMobile;
    }

    public boolean handleSiteSwitch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SitePreference sitePreference = this.sitePreferenceHandler.handleSitePreference(request, response);
        Device device = DeviceUtils.getRequiredCurrentDevice(request);
        if (this.mobileSiteUrlFactory != null && this.mobileSiteUrlFactory.isRequestForSite(request)) {
            if (ResolverUtils.isTablet(device, sitePreference) && this.tabletSiteUrlFactory != null) {
                response.sendRedirect(response.encodeRedirectURL(this.tabletSiteUrlFactory.createSiteUrl(request)));
                return false;
            }

            if ((ResolverUtils.isNormal(device, sitePreference) || this.handleTabletIsNormal(device, sitePreference)) && this.normalSiteUrlFactory != null) {
                response.sendRedirect(response.encodeRedirectURL(this.normalSiteUrlFactory.createSiteUrl(request)));
                return false;
            }
        } else if (this.tabletSiteUrlFactory != null && this.tabletSiteUrlFactory.isRequestForSite(request)) {
            if (ResolverUtils.isNormal(device, sitePreference) && this.normalSiteUrlFactory != null) {
                response.sendRedirect(response.encodeRedirectURL(this.normalSiteUrlFactory.createSiteUrl(request)));
                return false;
            }

            if (ResolverUtils.isMobile(device, sitePreference) && this.mobileSiteUrlFactory != null) {
                response.sendRedirect(response.encodeRedirectURL(this.mobileSiteUrlFactory.createSiteUrl(request)));
                return false;
            }
        } else if (!ResolverUtils.isMobile(device, sitePreference) && !this.handleTabletIsMobile(device, sitePreference)) {
            if (ResolverUtils.isTablet(device, sitePreference) && this.tabletSiteUrlFactory != null) {
                response.sendRedirect(response.encodeRedirectURL(this.tabletSiteUrlFactory.createSiteUrl(request)));
                return false;
            }
        } else if (this.mobileSiteUrlFactory != null) {
            response.sendRedirect(response.encodeRedirectURL(this.mobileSiteUrlFactory.createSiteUrl(request)));
            return false;
        }

        return true;
    }

    private boolean handleTabletIsNormal(Device device, SitePreference sitePreference) {
        return sitePreference == SitePreference.TABLET && !this.tabletIsMobile && (device.isTablet() || device.isMobile());
    }

    private boolean handleTabletIsMobile(Device device, SitePreference sitePreference) {
        return this.tabletIsMobile && (sitePreference == SitePreference.TABLET || sitePreference == null) && (device.isTablet() || device.isMobile());
    }
}
