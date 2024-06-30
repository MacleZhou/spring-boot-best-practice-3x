package org.springframework.mobile.device;

import jakarta.servlet.http.HttpServletRequest;

import java.util.*;

public class LiteDeviceResolver implements DeviceResolver {
    private final List<String> mobileUserAgentPrefixes = new ArrayList();
    private final List<String> mobileUserAgentKeywords = new ArrayList();
    private final List<String> tabletUserAgentKeywords = new ArrayList();
    private final List<String> normalUserAgentKeywords = new ArrayList();
    private static final String[] KNOWN_MOBILE_USER_AGENT_PREFIXES = new String[]{"w3c ", "w3c-", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq", "bird", "blac", "blaz", "brew", "cell", "cldc", "cmd-", "dang", "doco", "eric", "hipt", "htc_", "inno", "ipaq", "ipod", "jigs", "kddi", "keji", "leno", "lg-c", "lg-d", "lg-g", "lge-", "lg/u", "maui", "maxo", "midp", "mits", "mmef", "mobi", "mot-", "moto", "mwbp", "nec-", "newt", "noki", "palm", "pana", "pant", "phil", "play", "port", "prox", "qwap", "sage", "sams", "sany", "sch-", "sec-", "send", "seri", "sgh-", "shar", "sie-", "siem", "smal", "smar", "sony", "sph-", "symb", "t-mo", "teli", "tim-", "tosh", "tsm-", "upg1", "upsi", "vk-v", "voda", "wap-", "wapa", "wapi", "wapp", "wapr", "webc", "winw", "winw", "xda ", "xda-"};
    private static final String[] KNOWN_MOBILE_USER_AGENT_KEYWORDS = new String[]{"blackberry", "webos", "ipod", "lge vx", "midp", "maemo", "mmp", "mobile", "netfront", "hiptop", "nintendo DS", "novarra", "openweb", "opera mobi", "opera mini", "palm", "psp", "phone", "smartphone", "symbian", "up.browser", "up.link", "wap", "windows ce"};
    private static final String[] KNOWN_TABLET_USER_AGENT_KEYWORDS = new String[]{"ipad", "playbook", "hp-tablet", "kindle", "Macintosh"};

    //Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.2 Safari/605.1.15
    //Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36
    //

    public LiteDeviceResolver() {
        this.init();
    }

    public LiteDeviceResolver(List<String> normalUserAgentKeywords) {
        this.init();
        this.normalUserAgentKeywords.addAll(normalUserAgentKeywords);
    }

    public Device resolveDevice(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
//sec-ch-ua-platform=macOS; sec-ch-ua-mobile=?0

        String keyword;
        if (userAgent != null) {
            userAgent = userAgent.toLowerCase();
            Iterator i$ = this.normalUserAgentKeywords.iterator();
            while(i$.hasNext()) {
                keyword = (String)i$.next();
                if (userAgent.contains(keyword)) {
                    return this.resolveFallback(request);
                }
            }
        }

        if (userAgent != null) {
            userAgent = userAgent.toLowerCase();
            if (userAgent.contains("android") && !userAgent.contains("mobile")) {
                return this.resolveWithPlatform(DeviceType.TABLET, DevicePlatform.ANDROID);
            }

            if (userAgent.contains("ipad") || userAgent.contains("macintosh")) {
                return this.resolveWithPlatform(DeviceType.TABLET, DevicePlatform.IOS);
            }

            if (userAgent.contains("silk") && !userAgent.contains("mobile")) {
                return this.resolveWithPlatform(DeviceType.TABLET, DevicePlatform.UNKNOWN);
            }

            Iterator tabletUserAgentKeywordIterator = this.tabletUserAgentKeywords.iterator();

            while(tabletUserAgentKeywordIterator.hasNext()) {
                keyword = (String)tabletUserAgentKeywordIterator.next();
                if (userAgent.contains(keyword)) {
                    return this.resolveWithPlatform(DeviceType.TABLET, DevicePlatform.UNKNOWN);
                }
            }
        }

        if (request.getHeader("x-wap-profile") == null && request.getHeader("Profile") == null) {
            String accept;
            if (userAgent != null && userAgent.length() >= 4) {
                accept = userAgent.substring(0, 4).toLowerCase();
                if (this.mobileUserAgentPrefixes.contains(accept)) {
                    return this.resolveWithPlatform(DeviceType.MOBILE, DevicePlatform.UNKNOWN);
                }
            }

            accept = request.getHeader("Accept");
            if (accept != null && accept.contains("wap")) {
                return this.resolveWithPlatform(DeviceType.MOBILE, DevicePlatform.UNKNOWN);
            } else {
                //String keyword;
                if (userAgent != null) {
                    if (userAgent.contains("android")) {
                        return this.resolveWithPlatform(DeviceType.MOBILE, DevicePlatform.ANDROID);
                    }

                    if (userAgent.contains("iphone") || userAgent.contains("ipod") || userAgent.contains("ipad")) {
                        return this.resolveWithPlatform(DeviceType.MOBILE, DevicePlatform.IOS);
                    }

                    Iterator mobileUserAgentKeywordIteration = this.mobileUserAgentKeywords.iterator();

                    while(mobileUserAgentKeywordIteration.hasNext()) {
                        keyword = (String)mobileUserAgentKeywordIteration.next();
                        if (userAgent.contains(keyword)) {
                            return this.resolveWithPlatform(DeviceType.MOBILE, DevicePlatform.UNKNOWN);
                        }
                    }
                }

                Enumeration headers = request.getHeaderNames();

                do {
                    if (!headers.hasMoreElements()) {
                        return this.resolveFallback(request);
                    }

                    keyword = (String)headers.nextElement();
                } while(!keyword.contains("OperaMini"));

                return this.resolveWithPlatform(DeviceType.MOBILE, DevicePlatform.UNKNOWN);
            }
        } else {
            if (userAgent != null) {
                if (userAgent.contains("android")) {
                    return this.resolveWithPlatform(DeviceType.MOBILE, DevicePlatform.ANDROID);
                }

                if (userAgent.contains("iphone") || userAgent.contains("ipod") || userAgent.contains("ipad")) {
                    return this.resolveWithPlatform(DeviceType.MOBILE, DevicePlatform.IOS);
                }
            }

            return this.resolveWithPlatform(DeviceType.MOBILE, DevicePlatform.UNKNOWN);
        }
    }

    protected Device resolveWithPlatform(DeviceType deviceType, DevicePlatform devicePlatform) {
        return LiteDevice.from(deviceType, devicePlatform);
    }

    protected List<String> getMobileUserAgentPrefixes() {
        return this.mobileUserAgentPrefixes;
    }

    protected List<String> getMobileUserAgentKeywords() {
        return this.mobileUserAgentKeywords;
    }

    protected List<String> getTabletUserAgentKeywords() {
        return this.tabletUserAgentKeywords;
    }

    protected List<String> getNormalUserAgentKeywords() {
        return this.normalUserAgentKeywords;
    }

    protected void init() {
        this.getMobileUserAgentPrefixes().addAll(Arrays.asList(KNOWN_MOBILE_USER_AGENT_PREFIXES));
        this.getMobileUserAgentKeywords().addAll(Arrays.asList(KNOWN_MOBILE_USER_AGENT_KEYWORDS));
        this.getTabletUserAgentKeywords().addAll(Arrays.asList(KNOWN_TABLET_USER_AGENT_KEYWORDS));
    }

    protected Device resolveFallback(HttpServletRequest request) {
        return LiteDevice.NORMAL_INSTANCE;
    }
}
