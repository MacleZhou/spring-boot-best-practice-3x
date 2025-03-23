package com.macle.security.sdk.service;

/**
 * 直接从数据库中加载用户权限，注意Lock和Cache
 * */

import com.macle.security.sdk.annotation.ConditionalOnClassname;
import com.macle.security.sdk.annotation.ConditionalOnVariableTrue;
import com.macle.security.sdk.model.Permission;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@ConditionalOnVariableTrue("security.authorization.enabled")
@ConditionalOnClassname(value="security.authorization.user.permission.loader")
public class DatabaseUserPermissionLoader implements UserPermissionLoader {
    @Value("${secured.application.id}")
    private String applicationId;

    @Value("${secured.application.type}")
    private String applicationType;

    @Value("${security.authorization.resource.cache}")
    private String cacheMethod = "LOCAL";

    @Override
    public Collection<Permission> loadPermissions(String userId) throws Exception {
        //TODO
        return null;
    }

    @Override
    public Permission loadPermission(String userId, String permissionId) throws Exception{
        //TODO

        return null;
    }
}
