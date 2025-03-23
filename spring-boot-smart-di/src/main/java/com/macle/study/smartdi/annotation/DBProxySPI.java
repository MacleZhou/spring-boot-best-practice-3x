package com.macle.study.smartdi.annotation;

import com.burukeyou.smartdi.proxyspi.spi.ProxySPI;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ProxySPI(DbProxyFactory.class) // 指定配置获取逻辑
public @interface DBProxySPI {
    String value();
}