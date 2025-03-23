package com.macle.download.concept.controller;

import org.springframework.web.bind.annotation.RestController;
import com.macle.download.concept.service.DeviceService;
import com.macle.download.concept.model.Device;
import com.github.linyuzai.download.core.annotation.Download;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
public class DeviceDownloadController {

    private final DeviceService deviceService;

    public DeviceDownloadController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Download(filename = "二维码.zip")
    @GetMapping("/download")
    public List<Device> downloadDevices() {
        return deviceService.all();
    }
}
