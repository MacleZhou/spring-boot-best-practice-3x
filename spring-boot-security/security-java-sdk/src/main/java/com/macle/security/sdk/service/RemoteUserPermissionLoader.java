package com.macle.security.sdk.service;

/**
 * 调用远程服务加载需要用户的权限配置，注意lock/cache
 * */

import com.macle.security.sdk.model.Permission;

import java.util.Collection;

public class RemoteUserPermissionLoader implements UserPermissionLoader {
    @Override
    public Collection<Permission> loadPermissions(String userId) throws Exception {
        //TODO
        return null;
    }

    @Override
    public Permission loadPermission(String userId, String permissionId) throws Exception {
        //TODO
        return null;
    }
}
