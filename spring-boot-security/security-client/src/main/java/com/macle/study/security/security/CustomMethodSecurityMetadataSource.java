package com.macle.study.security.security;

import com.macle.study.security.service.SomeService;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.access.prepost.PreInvocationAttribute;
import org.springframework.security.access.prepost.PrePostInvocationAttributeFactory;

import java.lang.reflect.Method;
import java.util.*;

public class CustomMethodSecurityMetadataSource implements MethodSecurityMetadataSource {

    private final PrePostInvocationAttributeFactory attributeFactory;

    private final Map<Method, String> methodSpelExpressions = new HashMap<>();

    private final Set<Class<?>> supportClazz = new HashSet<>();


    public CustomMethodSecurityMetadataSource(PrePostInvocationAttributeFactory attributeFactory) {
        this.attributeFactory = attributeFactory;
        try{
            //模拟从数据库中读取方法的SpEL表达式并填充到methodSpelExpressions中
            methodSpelExpressions.put(SomeService.class.getMethod("preAuthorizeService"), "hasRole('ROLE_USER')");
            supportClazz.add(SomeService.class);
        } catch (NoSuchMethodException noSuchMethodException){

        }
    }

    public final Collection<ConfigAttribute> getAttributes(Object object) {
        if (object instanceof MethodInvocation mi) {
            Object target = mi.getThis();
            Class<?> targetClass = null;
            if (target != null) {
                targetClass = target instanceof Class ? (Class)target : AopProxyUtils.ultimateTargetClass(target);
            }

            Collection<ConfigAttribute> attrs = this.getAttributes(mi.getMethod(), targetClass);
            if (attrs != null && !attrs.isEmpty()) {
                return attrs;
            } else {
                if (target != null && !(target instanceof Class)) {
                    attrs = this.getAttributes(mi.getMethod(), target.getClass());
                }

                return attrs;
            }
        } else {
            throw new IllegalArgumentException("Object must be a non-null MethodInvocation");
        }
    }

    public final boolean supports(Class<?> clazz) {
        return supportClazz.contains(clazz);
    }


    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Method method, Class<?> targetClass) throws IllegalAccessError{
        ArrayList<ConfigAttribute> attrs = new ArrayList(2);
        String spelExpression = methodSpelExpressions.get(method);
        if(spelExpression != null) {
            PreInvocationAttribute pre = this.attributeFactory.createPreInvocationAttribute(null, null, spelExpression);
            if (pre != null) {
                attrs.add(pre);
            }
        }
        return attrs;
    }
}
