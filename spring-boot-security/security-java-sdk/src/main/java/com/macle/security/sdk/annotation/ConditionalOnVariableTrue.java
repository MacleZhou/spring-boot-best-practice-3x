package com.macle.security.sdk.annotation;

/**
 * 根据一个变量配置的类名决定是否加载当前bean，如果配置的变量值对应的值是true，则加载
 * */

import com.macle.security.sdk.config.ClassNameCondition;
import com.macle.security.sdk.config.VariableValueTrueCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Conditional(VariableValueTrueCondition.class)
public @interface ConditionalOnVariableTrue {
    String value() default "";
}
