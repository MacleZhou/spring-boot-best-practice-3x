package com.macle.security.test.service;

import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Map;

@RestControllerAdvice
public class RequestMappingLogger {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @PostConstruct
    public void logAllEndpoints() {
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        map.forEach((info, handlerMethod) -> {
            RequestMapping requestMapping = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), RequestMapping.class);
            if (requestMapping != null) {
                String[] paths = requestMapping.value();
                String methodName = handlerMethod.getMethod().getName();
                System.out.println("Path: " + String.join(", ", paths) + ", Method: " + methodName);
            }
        });
    }
}