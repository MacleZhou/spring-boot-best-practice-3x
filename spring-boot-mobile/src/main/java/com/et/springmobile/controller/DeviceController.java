package com.et.springmobile.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DevicePlatform;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@Data
@Slf4j
@RestController
public class DeviceController {

    @Value("${app.version}")
    private String version;

    @RequestMapping("/device/{name}")
    public String home(Device device,
                       @PathVariable(value = "name")String name) {
        StringBuffer restSb = new StringBuffer("输入用户:").append(name);
        if (device.isMobile()) {
            DevicePlatform devicePlatform = device.getDevicePlatform();
            log.info("Hello mobile user!");
            restSb.append("<br/>").append("用户为手机用户")
                    .append("<br/>").append("使用设备平台为：").append(devicePlatform.name());
        } else if (device.isTablet()) {
            log.info("Hello tablet user!");
            restSb.append("<br/>").append("用户为平板电脑用户");
        } else {
            log.info("Hello desktop user!");
            restSb.append("<br/>").append("用户为电脑用户");
        }
        return restSb.toString();
    }

    @RequestMapping("/device2/{name}")
    public String home2(HttpServletRequest servletRequest,
                        @PathVariable(value = "name")String name) {
        Device currentDevice = DeviceUtils.getCurrentDevice(servletRequest);

        StringBuffer restSb = new StringBuffer("输入用户:").append(name);
        if (currentDevice.isMobile()) {
            DevicePlatform devicePlatform = currentDevice.getDevicePlatform();
            log.info("Hello mobile user!");
            restSb.append("<br/>").append("用户为手机用户")
                    .append("<br/>").append("使用设备平台为：").append(devicePlatform.name());
        } else if (currentDevice.isTablet()) {
            log.info("Hello tablet user!");
            restSb.append("<br/>").append("用户为平板电脑用户");
        } else {
            log.info("Hello desktop user!");
            restSb.append("<br/>").append("用户为电脑用户");
        }
        return restSb.toString();
    }

}
