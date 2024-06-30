package org.springframework.mobile.device.switcher;

import jakarta.servlet.http.HttpServletRequest;

public abstract class AbstractSiteUrlFactory implements SiteUrlFactory {
    public AbstractSiteUrlFactory() {
    }

    protected String optionalPort(HttpServletRequest request) {
        return (!"http".equals(request.getScheme()) || request.getServerPort() == 80) && (!"https".equals(request.getScheme()) || request.getServerPort() == 443) ? null : ":" + request.getServerPort();
    }

    protected String createSiteUrlInternal(HttpServletRequest request, String serverName, String path) {
        StringBuilder builder = new StringBuilder();
        builder.append(request.getScheme()).append("://").append(serverName);
        String optionalPort = this.optionalPort(request);
        if (optionalPort != null) {
            builder.append(optionalPort);
        }

        builder.append(path);
        if (request.getQueryString() != null) {
            builder.append('?').append(request.getQueryString());
        }

        return builder.toString();
    }
}
