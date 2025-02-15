package com.macle.study.conditional.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

public class ApiCondition2 implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Map<String, Object> attrs = metadata.getAnnotationAttributes(ConditionalOnApi2.class.getName()) ;
        String key = (String) attrs.get("value") ;
        System.err.println(key);
        Environment env = context.getEnvironment() ;
        return "true".equals(env.getProperty(key)) ;
    }
}