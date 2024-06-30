package org.springframework.mobile.device;

public interface Device {
    boolean isNormal();

    boolean isMobile();

    boolean isTablet();

    DevicePlatform getDevicePlatform();
}
