package com.macle.security.sdk.service;

/**
 * 此接口处理用户权限加载， 用户权限可以是定义在本地的JSON文件中，也可以从本地数据库加载，或者从远程系统加载
 * */

import com.macle.security.sdk.model.Permission;

import java.util.Collection;


public interface UserPermissionLoader {
    Collection<Permission> loadPermissions(String userId) throws Exception;

    Permission loadPermission(String userId, String permissionId) throws Exception;
}