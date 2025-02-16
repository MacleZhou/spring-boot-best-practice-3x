package com.macle.security.sdk.service;

import cn.hutool.core.text.CharSequenceUtil;
import com.macle.security.sdk.interceptor.DynamicProxy;
import com.macle.security.sdk.interceptor.OperateEvent;
import com.macle.security.sdk.interceptor.AuthorityAdvice;
import com.macle.security.sdk.interceptor.SecuredAdvisor;
import com.macle.security.sdk.model.SecuredResource;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SecurityProxyService {

    @Resource
    private DynamicProxy dynamicProxy;

    @Resource
    private AuthorityAdvice authorityAdvice;

    @Resource
    private UserPermissionLoader userPermissionLoader;

    @Resource
    private SecuredResourceLoader securedResourceLoader;


    /**
     * 存放需要安全拦截的资源代理对象
     * Key examples:
     *  1) com.macle.security.sdk.interceptor.PreAuthorityAdvice#execution(* com.macle.study.tmp.controller.HelloController.hello(..))
     *  2) com.macle.security.sdk.interceptor.PreAuthorityAdvice#com.macle.security.sdk.annotation.SecuredResource
     * */
    private Map<String, SecuredAdvisor> cachedSecureddvisorMap = new ConcurrentHashMap<>();



    /**
     * 系统启动时处理需要拦截的所有方法，拦截方法有如下两种标识方式
     * 1. 从RBAC系统配置，这种方式在客户端是0代码侵入
     * 2. 通过对Spring的@Component标识的所有方法上添加@SecuredResource
     *
     * 对于所有需要拦截的方法对应的对象生成代理对象。生成代理对象时，需要找到类中的
     * 需要拦截的方法，进行PreAuthorityAdvice或PostAuthorityAdvice增强
     *
     * //TODO - PostAuthorityAdvice
     * */
    public void initSecuredResources() throws Exception {
        MethodInterceptor preAuthorityAdvice = new AuthorityAdvice();

        //2. 扫描所有带有@SecuredResource的方法，生成代理对象
        List<SecuredResource> securedResources = securedResourceLoader.getSecuredResources();
        if(securedResources != null && !securedResources.isEmpty()) {
            for (SecuredResource securedResource : securedResources) {
                this.add(preAuthorityAdvice, securedResource.getExpression(), null);
            }
        }
    }

    /**
     * 安装expression或者annotation增加Advise
     * @param advice - 增强的类，调用类中的invoke方法, 比如 com.macle.security.sdk.interceptor.PreAuthorityAdvice
     * @param expression - 表达式型的增强点, 比如execution(public com.macle.security.sdk.test.service.TestService.approval(..))
     * @param annotationClass - Annotation标识的增强点，比如annotation(com.macle.security.sdk.annotation.Secured.class)
     * */
    private String add(MethodInterceptor advice, String expression, String annotationClass) throws Exception {
        if(CharSequenceUtil.isAllBlank(expression, annotationClass)) {
            return "the parameter is abnormal";
        }

        String annotationClassKey = advice.getClass().getCanonicalName() + "#" + annotationClass;
        String expressionClassKey = advice.getClass().getCanonicalName() + "#" + expression;
        if(cachedSecureddvisorMap.containsKey(annotationClassKey) || cachedSecureddvisorMap.containsKey(expressionClassKey)) {
            return "the advisor is already added";
        }

        SecuredAdvisor securedAdvisor;
        String advisorKey = annotationClassKey;
        //1️⃣注解为主，有注解就用注解
        if(CharSequenceUtil.isNotBlank(annotationClass)){
            Class<? extends Annotation> aClass = (Class<? extends Annotation>) Class.forName(annotationClass);
            securedAdvisor = new SecuredAdvisor(aClass, advice);
            cachedSecureddvisorMap.put(annotationClassKey, securedAdvisor);
        } else {
            securedAdvisor = new SecuredAdvisor(expression, advice);
            cachedSecureddvisorMap.put(expressionClassKey, securedAdvisor);
            advisorKey = expressionClassKey;
        }

        dynamicProxy.operateAdvisor(securedAdvisor, OperateEvent.ADD);
        return "advisor add success, advisor key: " + advisorKey;
    }

    private String delete(MethodInterceptor advice, String expression, String annotationClass) throws Exception {
        if(CharSequenceUtil.isAllBlank(expression, annotationClass)) {
            return "the parameter is abnormal";
        }

        String annotationClassKey = advice.getClass().getCanonicalName() + "#" + annotationClass;
        String expressionClassKey = advice.getClass().getCanonicalName() + "#" + expression;
        if(!cachedSecureddvisorMap.containsKey(annotationClassKey) && !cachedSecureddvisorMap.containsKey(expressionClassKey)) {
            return "the advisor doesn't exists";
        }

        //1️⃣注解为主，有注解就用注解
        SecuredAdvisor securedAdvisor = null;
        String advisorKey = annotationClassKey;
        if(cachedSecureddvisorMap.containsKey(annotationClassKey)) {
            securedAdvisor = cachedSecureddvisorMap.get(annotationClassKey);
        } else {
            securedAdvisor = cachedSecureddvisorMap.get(expressionClassKey);
            advisorKey = expressionClassKey;
        }
        dynamicProxy.operateAdvisor(securedAdvisor, OperateEvent.DELETE);
        cachedSecureddvisorMap.remove(advisorKey);

        return "advisor delete success, advisor key: " + advisorKey;
    }


}