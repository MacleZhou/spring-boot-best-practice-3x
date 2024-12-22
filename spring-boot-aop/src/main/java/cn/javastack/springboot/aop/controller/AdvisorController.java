package cn.javastack.springboot.aop.controller;

import cn.hutool.core.text.CharSequenceUtil;
import cn.javastack.springboot.aop.plugin.DynamicProxy;
import cn.javastack.springboot.aop.plugin.OperateEvent;
import cn.javastack.springboot.aop.plugin.XdxAdvisor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/advisor")
@Slf4j
public class AdvisorController {
    @Resource
    private DynamicProxy dynamicProxy;

    private static Map<String, XdxAdvisor> xdxAdvisorMap = new ConcurrentHashMap<>();

    @GetMapping("/add")
    public String add(String interceptorClass, String expression, String annotationClass) throws Exception {
        if(CharSequenceUtil.isAllBlank(expression, annotationClass) || CharSequenceUtil.isBlank(interceptorClass)) {
            return "the parameter is abnormal";
        }

        String annotationClassKey = interceptorClass + "#" + annotationClass;
        String expressionClassKey = interceptorClass + "#" + expression;
        if(xdxAdvisorMap.containsKey(annotationClassKey) || xdxAdvisorMap.containsKey(expressionClassKey)) {
            return "the advisor is already added";
        }

        MethodInterceptor methodInterceptor = (MethodInterceptor) Class.forName(interceptorClass).getDeclaredConstructor().newInstance();
        XdxAdvisor xdxAdvisor;
        String advisorKey = annotationClassKey;
        //1️⃣注解为主，有注解就用注解
        if(CharSequenceUtil.isNotBlank(annotationClass)){
            Class<? extends Annotation> aClass = (Class<? extends Annotation>) Class.forName(annotationClass);
            xdxAdvisor = new XdxAdvisor(aClass, methodInterceptor);
            xdxAdvisorMap.put(annotationClassKey, xdxAdvisor);
        } else {
            xdxAdvisor = new XdxAdvisor(expression, methodInterceptor);
            xdxAdvisorMap.put(expressionClassKey, xdxAdvisor);
            advisorKey = expressionClassKey;
        }

        dynamicProxy.operateAdvisor(xdxAdvisor, OperateEvent.ADD);
        return "advisor add success, advisor key: " + advisorKey;
    }

    @GetMapping("/delete")
    public String delete(String interceptorClass, String expression, String annotationClass) throws Exception {
        if(CharSequenceUtil.isAllBlank(expression, annotationClass) || CharSequenceUtil.isBlank(interceptorClass)) {
            return "the parameter is abnormal";
        }

        String annotationClassKey = interceptorClass + "#" + annotationClass;
        String expressionClassKey = interceptorClass + "#" + expression;
        if(!xdxAdvisorMap.containsKey(annotationClassKey) && !xdxAdvisorMap.containsKey(expressionClassKey)) {
            return "the advisor doesn't exists";
        }

        //1️⃣注解为主，有注解就用注解
        XdxAdvisor xdxAdvisor = null;
        String advisorKey = annotationClassKey;
        if(xdxAdvisorMap.containsKey(annotationClassKey)) {
            xdxAdvisor = xdxAdvisorMap.get(annotationClassKey);
        } else {
            xdxAdvisor = xdxAdvisorMap.get(expressionClassKey);
            advisorKey = expressionClassKey;
        }
        dynamicProxy.operateAdvisor(xdxAdvisor, OperateEvent.DELETE);
        xdxAdvisorMap.remove(advisorKey);

        return "advisor delete success, advisor key: " + advisorKey;
    }
}
