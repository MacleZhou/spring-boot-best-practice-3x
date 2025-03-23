package com.macle.security.sdk.config;

/**
 * 判断当前类是否与@ConditionalOnClassname中的value变量值标识的类名相同，用于决定是否加载当前bean
 * */

import com.macle.security.sdk.annotation.ConditionalOnClassname;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

public class VariableValueEqualCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Map<String, Object> attrs = metadata.getAnnotationAttributes(ConditionalOnClassname.class.getName());
        String variable = (String) attrs.get("variable");
        String value = (String) attrs.get("value");
        Environment environment = context.getEnvironment();
        return value.equals(environment.getProperty(variable));
    }
}
