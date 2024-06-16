package com.macle.swagger2.annotations;

import java.lang.annotation.*;

// 使用Java8的"重复性注解"特性, 优化使用体验.
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface FlowTypes {
    // 流程类型的名称
    String name() default "";

    FlowType[] value();
}