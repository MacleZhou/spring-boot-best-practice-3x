package com.atshuo.audit.aop.dto;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditLogTag {


    /**
     * 系统模块
     */
    String model() default "";


    /**
     * 具体业务标签
     */
    String tag() default "";

}