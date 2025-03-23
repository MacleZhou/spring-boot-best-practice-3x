package com.macle.download.concept.model;

import com.github.linyuzai.download.core.source.reflect.SourceName;
import com.github.linyuzai.download.core.source.reflect.SourceObject;

public class Device {
    private String name;

    @SourceObject
    private String qrCodeUrl;

    @SourceName
    public String getQrCodeName() {
        return name + ".png";
    }
}