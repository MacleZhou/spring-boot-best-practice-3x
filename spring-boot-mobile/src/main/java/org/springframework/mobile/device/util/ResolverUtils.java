package org.springframework.mobile.device.util;

import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.site.SitePreference;

public class ResolverUtils {
    public static boolean isNormal(Device device, SitePreference sitePreference) {
        return sitePreference == SitePreference.NORMAL || device == null || device.isNormal() && sitePreference == null;
    }

    public static boolean isMobile(Device device, SitePreference sitePreference) {
        return sitePreference == SitePreference.MOBILE || device != null && device.isMobile() && sitePreference == null;
    }

    public static boolean isTablet(Device device, SitePreference sitePreference) {
        return sitePreference == SitePreference.TABLET || device != null && device.isTablet() && sitePreference == null;
    }

    private ResolverUtils() {
    }
}
