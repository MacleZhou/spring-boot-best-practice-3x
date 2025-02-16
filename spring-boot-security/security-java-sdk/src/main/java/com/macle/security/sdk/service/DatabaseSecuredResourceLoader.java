package com.macle.security.sdk.service;

/**
 * 直接从数据库中加载需要安全控制的资源，注意Lock和Cache
 * */
import com.macle.security.sdk.config.SecurityConfig;
import com.macle.security.sdk.model.SecuredResource;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DatabaseSecuredResourceLoader implements ExternalSecuredResourceLoader {

    @Resource
    private SecurityConfig securityConfig;

    @Override
    public List<SecuredResource> loadSecuredResources() throws Exception {
        return null;
    }
}
