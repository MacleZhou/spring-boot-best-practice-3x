package com.macle.security.sdk.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter @Getter
public class Permission {
    /**
     * 权限ID，不能为空
     * */
    private String id;
    /**
     * 权限描述
     * */
    private String description;

    /**
     * 资源ID，可以为空，当资源ID为空时，配置同一权限ID下的所有资源，即为相同的权限
     * */
    private String resourceId;

    /**
     * 是否允许访问资源
     * */
    private boolean allowAccess;

    /**
     * 拥有该资源的事前数据权限，当资源定义为 preDataAuthority = true时（即调用前需要数据权限检查），必须有人一至少一个数据权限通过才能访问
     * */
    private List<DataPermission> preDataPermissions;

    /**
     * 拥有该资源的事后数据权限，当资源定义为 postDataAuthority = true时（即调用后需要数据权限检查），必须有人一至少一个数据权限通过才能访问
     * */
    private List<DataPermission> postDataPermissions;
}