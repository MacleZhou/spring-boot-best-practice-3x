package com.macle.security.sdk.service;

import com.macle.security.sdk.annotation.SecuredPoint;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SecuredResourceFinder implements BeanPostProcessor {
    List<Method> securedResourceMethods = new ArrayList<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean; // 通常不做改变，只是用来触发方法检查
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(Component.class)) {
            Method[] methods = bean.getClass().getDeclaredMethods();
            Arrays.stream(methods)
                    .filter(method -> method.isAnnotationPresent(SecuredPoint.class))
                    .forEach(method -> {
                        System.out.println("Found method: " + method.getName());
                        securedResourceMethods.add(method);
                    });
        }
        return bean;
    }

    public List<Method> getSecuredResourceMethods() {
        return securedResourceMethods;
    }
}
