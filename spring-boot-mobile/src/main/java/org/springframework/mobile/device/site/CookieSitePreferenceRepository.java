package org.springframework.mobile.device.site;

import org.springframework.web.util.CookieGenerator;
import org.springframework.web.util.WebUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieSitePreferenceRepository extends CookieGenerator implements SitePreferenceRepository {
    private static final String DEFAULT_COOKIE_NAME = CookieSitePreferenceRepository.class.getName() + ".SITE_PREFERENCE";

    public CookieSitePreferenceRepository() {
        this.setCookieName(DEFAULT_COOKIE_NAME);
    }

    public CookieSitePreferenceRepository(String cookieDomain) {
        this.setCookieName(DEFAULT_COOKIE_NAME);
        this.setCookieDomain(cookieDomain);
    }

    @Override
    public void saveSitePreference(SitePreference preference, HttpServletRequest request, HttpServletResponse response) {
        this.addCookie(response, preference.name());
    }

    @Override
    public SitePreference loadSitePreference(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, this.getCookieName());
        return cookie != null ? SitePreference.valueOf(cookie.getValue()) : null;
    }
}