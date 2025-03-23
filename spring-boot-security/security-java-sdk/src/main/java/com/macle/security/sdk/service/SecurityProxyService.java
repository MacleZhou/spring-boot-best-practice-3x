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
     * 通过此方法分析Spring components的依赖顺序，通过依赖顺序定义创建代理对象的先后顺序。比如有一个TestController通过@Autowired或@Resource或
     * 者构造方法等方式依赖了一个TestService的bean，则当TestService本身也需要被代理时，处理起来就比较复杂，需要先生成TestService的代理对象，然后
     * 把TestController中对TestService的依赖指向新创建的TestService的代理对象，否则TestController中引用的任然是代理前TestService bean。以下
     * 是一些确定顺序的关键信息
     * 1. 只需要考虑通过@Component/@RestController/@Controller/@Service等标识的对象，@Repository的不需要考虑
     * 2. @RestController或@Controller标识的可以总是最后处理
     * 3. 如果一个Bean已经是Advised的实例，即已经被代理过了，则可以第一个处理，因为不会在创建新的代理对象，只会在原代理对象上增加代理逻辑
     *
     * 备注，查找依赖时，如果一个对象已经被代理过，这不能
     * */
    private void analysisDependencies(List<SecuredResource> securedResources){

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