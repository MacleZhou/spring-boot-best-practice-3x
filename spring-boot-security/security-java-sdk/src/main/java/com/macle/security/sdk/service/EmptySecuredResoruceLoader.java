package com.macle.security.sdk.service;

import com.macle.security.sdk.config.SecurityConfig;
import com.macle.security.sdk.model.SecuredResource;
import jakarta.annotation.Resource;

import java.util.List;

public class EmptySecuredResoruceLoader implements ExternalSecuredResourceLoader {
    @Resource
    private SecurityConfig securityConfig;

    @Override
    public List<SecuredResource> loadSecuredResources() throws Exception {
        return null;
    }
}
