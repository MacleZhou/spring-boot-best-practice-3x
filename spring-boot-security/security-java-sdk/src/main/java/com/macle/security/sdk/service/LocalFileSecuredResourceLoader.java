package com.macle.security.sdk.service;

import com.alibaba.fastjson2.JSON;
import com.macle.security.sdk.config.SecurityConfig;
import com.macle.security.sdk.model.SecuredResource;
import jakarta.annotation.Resource;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.List;

@Component
public class LocalFileSecuredResourceLoader implements ExternalSecuredResourceLoader {

    @Resource
    private SecurityConfig securityConfig;

    @Override
    public List<SecuredResource> loadSecuredResources() throws Exception {
        File file = ResourceUtils.getFile(securityConfig.getSecuredResourceFile());
        String json = FileUtils.readFileToString(file, "UTF-8");
        List<SecuredResource> securedResources = JSON.parseArray(json, SecuredResource.class);
        return securedResources;
    }
}
