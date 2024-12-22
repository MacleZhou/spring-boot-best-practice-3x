package com.macle.study.anti.shaking.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @description 加上这个注解可以指定要防抖的方法
 */
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RequestLock {
    public String prefix();

    public String delimiter() default "&";

    public TimeUnit timeUnit() default TimeUnit.MICROSECONDS;

    public long expire() default 5000;
}
