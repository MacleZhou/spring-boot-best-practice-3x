package com.macle.security.sdk.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 此SecuredPoint用于标识一个方法或者一个类有权限拦截检查，如果一个类或者方法有此标识，则可以不用配置在RBAC中，在
 * RBAC中，只用配置用户权限，按照permissionId进行配置即可
 * */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SecuredPoint {

    /**
     * 功能名称，用于日志打印
     * */
    String resourceName() default "";

    /**
     * 权限ID，用于匹配用户的权限配置
     * */
    String permissionId() default "";

    /**
     * 是否需要访问权限，当定义为true，如果没有访问权限，则抛出AuthorizationDeniedException
     * */
    boolean accessControl() default true;

    /**
     * 访问/调用资源前是否需要检查数据权限，当定义为true，如果没有访问权限，则抛出AuthorizationDeniedException
     * */
    boolean preDataAuthority() default false;

    /**
     * 访问/调用资源后是否需要检查数据权限，当定义为true，则会根据定义进行数据结果进行
     * 1. 判断 - 如果不满足判断条件，则抛出AuthorizationDeniedException
     * 2. 或过滤 - 比如customer.vip=false，则会从结果集合中保留customer.vip=false的记录返回，其它vip=true的则不会返回
     * 3. 或mask - 比如姓名 Macle Zhou, 变成"* Zhou"
     *
     * 由于暂时没有具体的事后权限需求，暂时不会实现
     * */
    boolean postDataAuthority() default false;
}
