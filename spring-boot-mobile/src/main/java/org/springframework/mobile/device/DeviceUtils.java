package org.springframework.mobile.device;

import org.springframework.web.context.request.RequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

public class DeviceUtils {
    public static final String CURRENT_DEVICE_ATTRIBUTE = "currentDevice";

    public DeviceUtils() {
    }

    public static Device getCurrentDevice(HttpServletRequest request) {
        return (Device)request.getAttribute("currentDevice");
    }

    public static Device getRequiredCurrentDevice(HttpServletRequest request) {
        Device device = getCurrentDevice(request);
        if (device == null) {
            throw new IllegalStateException("No currenet device is set in this request and one is required - have you configured a DeviceResolvingHandlerInterceptor?");
        } else {
            return device;
        }
    }

    public static Device getCurrentDevice(RequestAttributes attributes) {
        return (Device)attributes.getAttribute("currentDevice", 0);
    }
}
