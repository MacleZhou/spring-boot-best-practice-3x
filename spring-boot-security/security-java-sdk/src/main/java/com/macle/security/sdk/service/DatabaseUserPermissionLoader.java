package com.macle.security.sdk.service;

/**
 * 直接从数据库中加载用户权限，注意Lock和Cache
 * */

import com.macle.security.sdk.config.SecurityConfig;
import com.macle.security.sdk.model.Permission;
import jakarta.annotation.Resource;

import java.util.Collection;

public class DatabaseUserPermissionLoader implements UserPermissionLoader {
    @Resource
    private SecurityConfig securityConfig;

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
