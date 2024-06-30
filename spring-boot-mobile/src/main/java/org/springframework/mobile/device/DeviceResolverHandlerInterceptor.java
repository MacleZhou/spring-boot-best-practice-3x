package org.springframework.mobile.device;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class DeviceResolverHandlerInterceptor implements HandlerInterceptor {
    private final DeviceResolver deviceResolver;

    public DeviceResolverHandlerInterceptor() {
        this(new LiteDeviceResolver());
    }

    public DeviceResolverHandlerInterceptor(DeviceResolver deviceResolver) {
        this.deviceResolver = deviceResolver;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Device device = this.deviceResolver.resolveDevice(request);
        request.setAttribute("currentDevice", device);
        return true;
    }
}