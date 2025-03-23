package com.macle.security.sdk.service;

/**
 * 直接从数据库中加载需要安全控制的资源，注意Lock和Cache
 * */
import com.macle.security.sdk.annotation.ConditionalOnClassname;
import com.macle.security.sdk.annotation.ConditionalOnVariableTrue;
import com.macle.security.sdk.model.SecuredResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@ConditionalOnVariableTrue("security.authorization.enabled")
@ConditionalOnClassname(value="security.authorization.resource.loader")
public class DatabaseSecuredResourceLoader implements ExternalSecuredResourceLoader {
    @Value("${secured.application.id}")
    private String applicationId;

    @Value("${secured.application.type}")
    private String applicationType;

    @Value("${security.authorization.resource.cache}")
    private String cacheMethod = "LOCAL";

    @Override
    public List<SecuredResource> loadSecuredResources() throws Exception {
        return null;
    }
}
