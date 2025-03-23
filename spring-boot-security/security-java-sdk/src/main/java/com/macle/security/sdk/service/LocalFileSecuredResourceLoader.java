package com.macle.security.sdk.service;

import com.alibaba.fastjson2.JSON;
import com.macle.security.sdk.annotation.ConditionalOnClassname;
import com.macle.security.sdk.annotation.ConditionalOnVariableTrue;
import com.macle.security.sdk.model.SecuredResource;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.List;

@Component
@ConditionalOnVariableTrue("security.authorization.enabled")
@ConditionalOnClassname(value="security.authorization.resource.loader")
public class LocalFileSecuredResourceLoader implements ExternalSecuredResourceLoader {

    @Value("${security.authorization.resource.file}")
    private String securedResourceFile;


    @Override
    public List<SecuredResource> loadSecuredResources() throws Exception {
        File file = ResourceUtils.getFile(securedResourceFile);
        String json = FileUtils.readFileToString(file, "UTF-8");
        List<SecuredResource> securedResources = JSON.parseArray(json, SecuredResource.class);
        return securedResources;
    }
}
