package com.macle.security.sdk.service;

import com.alibaba.fastjson2.JSON;
import com.macle.security.sdk.annotation.ConditionalOnClassname;
import com.macle.security.sdk.annotation.ConditionalOnVariableTrue;
import com.macle.security.sdk.model.Permission;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ConditionalOnVariableTrue("security.authorization.enabled")
@ConditionalOnClassname(value="security.authorization.user.permission.loader")
public class LocalFileUserPermissionLoader implements UserPermissionLoader {
    @Value("${security.authorization.user.permission.folder}")
    private String userPermissionFolder;

    Map<String, Map<String, Permission>> userPermissions = new ConcurrentHashMap<>();

    @Override
    public Collection<Permission> loadPermissions(String userId) throws Exception {
        if(!userPermissions.containsKey(userId)){
            loadPermissionsFromLocalFile(userId);
        }
        if(userPermissions.containsKey(userId)){
            return userPermissions.get(userId).values();
        }
        return null;
    }

    @Override
    public Permission loadPermission(String userId, String permissionId) throws Exception{
        if(!userPermissions.containsKey(userId)){
            loadPermissionsFromLocalFile(userId);
        }

        if(userPermissions.get(userId).containsKey(permissionId)) {
            return userPermissions.get(userId).get(permissionId);
        }

        return null;
    }

    private void loadPermissionsFromLocalFile(String userId) throws Exception {
        Map<String, Permission> permissionMap = new HashMap<>();
        userPermissions.put(userId, permissionMap);

        File file = ResourceUtils.getFile(userPermissionFolder + File.separator + userId + ".json");
        if(file.exists()){
            String json = FileUtils.readFileToString(file, "UTF-8");
            List<Permission> permissions = JSON.parseArray(json, Permission.class);

            for (Permission permission : permissions) {
                permissionMap.put(permission.getId(), permission);
            }
        }
    }
}
