package com.macle.download.concept.service;

import com.macle.download.concept.model.Device;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DeviceService {

    public List<Device> all(){
        return Arrays.asList(new Device());
    }
}
