package com.macle.security.sdk.service;

/**
 * 调用远程服务加载需要安全控制的资源，注意lock/cache
 * */

import com.macle.security.sdk.config.SecurityConfig;
import com.macle.security.sdk.model.SecuredResource;
import jakarta.annotation.Resource;

import java.util.List;

public class RemoteSecuredResourceLoader implements ExternalSecuredResourceLoader {

    @Resource
    private SecurityConfig securityConfig;

    @Override
    public List<SecuredResource> loadSecuredResources() throws Exception {
        //TODO - Load secured resources definition from remote RBAC
        return null;
    }
}
