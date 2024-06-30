package org.springframework.mobile.device;

class LiteDevice implements Device {
    public static final LiteDevice NORMAL_INSTANCE;
    public static final LiteDevice MOBILE_INSTANCE;
    public static final LiteDevice TABLET_INSTANCE;
    private final DeviceType deviceType;
    private final DevicePlatform devicePlatform;

    public boolean isNormal() {
        return this.deviceType == DeviceType.NORMAL;
    }

    public boolean isMobile() {
        return this.deviceType == DeviceType.MOBILE;
    }

    public boolean isTablet() {
        return this.deviceType == DeviceType.TABLET;
    }

    public DevicePlatform getDevicePlatform() {
        return this.devicePlatform;
    }

    public DeviceType getDeviceType() {
        return this.deviceType;
    }

    public static Device from(DeviceType deviceType, DevicePlatform devicePlatform) {
        return new LiteDevice(deviceType, devicePlatform);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[LiteDevice ");
        builder.append("type").append("=").append(this.deviceType);
        builder.append("]");
        return builder.toString();
    }

    private LiteDevice(DeviceType deviceType, DevicePlatform devicePlatform) {
        this.deviceType = deviceType;
        this.devicePlatform = devicePlatform;
    }

    static {
        NORMAL_INSTANCE = new LiteDevice(DeviceType.NORMAL, DevicePlatform.UNKNOWN);
        MOBILE_INSTANCE = new LiteDevice(DeviceType.MOBILE, DevicePlatform.UNKNOWN);
        TABLET_INSTANCE = new LiteDevice(DeviceType.TABLET, DevicePlatform.UNKNOWN);
    }
}
