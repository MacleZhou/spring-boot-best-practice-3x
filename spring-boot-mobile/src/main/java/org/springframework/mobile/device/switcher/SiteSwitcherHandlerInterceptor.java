package org.springframework.mobile.device.switcher;

import org.springframework.mobile.device.site.CookieSitePreferenceRepository;
import org.springframework.mobile.device.site.SitePreferenceHandler;
import org.springframework.mobile.device.site.StandardSitePreferenceHandler;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SiteSwitcherHandlerInterceptor implements HandlerInterceptor {
    private final SiteSwitcherHandler siteSwitcherHandler;

    public SiteSwitcherHandlerInterceptor(SiteUrlFactory normalSiteUrlFactory, SiteUrlFactory mobileSiteUrlFactory, SitePreferenceHandler sitePreferenceHandler) {
        this(normalSiteUrlFactory, mobileSiteUrlFactory, sitePreferenceHandler, false);
    }

    public SiteSwitcherHandlerInterceptor(SiteUrlFactory normalSiteUrlFactory, SiteUrlFactory mobileSiteUrlFactory, SitePreferenceHandler sitePreferenceHandler, Boolean tabletIsMobile) {
        this.siteSwitcherHandler = new StandardSiteSwitcherHandler(normalSiteUrlFactory, mobileSiteUrlFactory, (SiteUrlFactory)null, sitePreferenceHandler, tabletIsMobile);
    }

    public SiteSwitcherHandlerInterceptor(SiteUrlFactory normalSiteUrlFactory, SiteUrlFactory mobileSiteUrlFactory, SiteUrlFactory tabletSiteUrlFactory, SitePreferenceHandler sitePreferenceHandler) {
        this.siteSwitcherHandler = new StandardSiteSwitcherHandler(normalSiteUrlFactory, mobileSiteUrlFactory, tabletSiteUrlFactory, sitePreferenceHandler, (Boolean)null);
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return this.siteSwitcherHandler.handleSiteSwitch(request, response);
    }

    public static SiteSwitcherHandlerInterceptor mDot(String serverName) {
        return mDot(serverName, false);
    }

    public static SiteSwitcherHandlerInterceptor mDot(String serverName, Boolean tabletIsMobile) {
        return standard(serverName, "m." + serverName, "." + serverName, tabletIsMobile);
    }

    public static SiteSwitcherHandlerInterceptor dotMobi(String serverName) {
        return dotMobi(serverName, false);
    }

    public static SiteSwitcherHandlerInterceptor dotMobi(String serverName, Boolean tabletIsMobile) {
        int lastDot = serverName.lastIndexOf(46);
        return standard(serverName, serverName.substring(0, lastDot) + ".mobi", "." + serverName, tabletIsMobile);
    }

    public static SiteSwitcherHandlerInterceptor standard(String normalServerName, String mobileServerName, String cookieDomain) {
        return standard(normalServerName, mobileServerName, cookieDomain, false);
    }

    public static SiteSwitcherHandlerInterceptor standard(String normalServerName, String mobileServerName, String cookieDomain, Boolean tabletIsMobile) {
        return new SiteSwitcherHandlerInterceptor(new StandardSiteUrlFactory(normalServerName), new StandardSiteUrlFactory(mobileServerName), new StandardSitePreferenceHandler(new CookieSitePreferenceRepository(cookieDomain)), tabletIsMobile);
    }

    public static SiteSwitcherHandlerInterceptor standard(String normalServerName, String mobileServerName, String tabletServerName, String cookieDomain) {
        return new SiteSwitcherHandlerInterceptor(new StandardSiteUrlFactory(normalServerName), new StandardSiteUrlFactory(mobileServerName), new StandardSiteUrlFactory(tabletServerName), new StandardSitePreferenceHandler(new CookieSitePreferenceRepository(cookieDomain)));
    }

    public static SiteSwitcherHandlerInterceptor urlPath(String mobilePath) {
        return new SiteSwitcherHandlerInterceptor(new NormalSitePathUrlFactory(mobilePath), new MobileSitePathUrlFactory(mobilePath, (String)null), new StandardSitePreferenceHandler(new CookieSitePreferenceRepository()));
    }

    public static SiteSwitcherHandlerInterceptor urlPath(String mobilePath, String rootPath) {
        return new SiteSwitcherHandlerInterceptor(new NormalSitePathUrlFactory(mobilePath, rootPath), new MobileSitePathUrlFactory(mobilePath, (String)null, rootPath), new StandardSitePreferenceHandler(new CookieSitePreferenceRepository()));
    }

    public static SiteSwitcherHandlerInterceptor urlPath(String mobilePath, String tabletPath, String rootPath) {
        return new SiteSwitcherHandlerInterceptor(new NormalSitePathUrlFactory(mobilePath, tabletPath, rootPath), new MobileSitePathUrlFactory(mobilePath, tabletPath, rootPath), new TabletSitePathUrlFactory(tabletPath, mobilePath, rootPath), new StandardSitePreferenceHandler(new CookieSitePreferenceRepository()));
    }
}
