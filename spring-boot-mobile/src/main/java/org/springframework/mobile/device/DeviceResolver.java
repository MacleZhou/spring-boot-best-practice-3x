package org.springframework.mobile.device;

import jakarta.servlet.http.HttpServletRequest;

public interface DeviceResolver {
    Device resolveDevice(HttpServletRequest request);
}
