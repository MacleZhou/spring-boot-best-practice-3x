package org.springframework.mobile.device;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeviceResolverRequestFilter extends OncePerRequestFilter {
    private final DeviceResolver deviceResolver;

    public DeviceResolverRequestFilter() {
        this(new LiteDeviceResolver());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Device device = this.deviceResolver.resolveDevice(request);
        request.setAttribute("currentDevice", device);
        filterChain.doFilter(request, response);
    }

    public DeviceResolverRequestFilter(DeviceResolver deviceResolver) {
        this.deviceResolver = deviceResolver;
    }
}