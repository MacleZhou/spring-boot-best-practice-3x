package com.macle.security.sdk.service;

import cn.hutool.core.text.CharSequenceUtil;
import com.macle.security.sdk.annotation.SecuredPoint;
import com.macle.security.sdk.interceptor.DynamicProxy;
import com.macle.security.sdk.interceptor.OperateEvent;
import com.macle.security.sdk.interceptor.PreAuthorityAdvice;
import com.macle.security.sdk.interceptor.SecuredAdvisor;
import com.macle.security.sdk.model.ApplicationSecurityConfig;
import com.macle.security.sdk.model.Permission;
import com.macle.security.sdk.model.ResourceType;
import com.macle.security.sdk.model.SecuredResource;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SecurityService {

    private List<SecuredResource> securedResourcesCache = null;

    /**
     * 用于缓存一个资源的拦截定义
     * Key - 是一个精确匹配的方法描述，通过调用方法的toString()生成，比如
     * public java.lang.String com.macle.study.springboot.aop.controller.AdvisorController.delete(java.lang.String,java.lang.String,java.lang.String) throws java.lang.Exception
     */
    private Map<String, SecuredResource> cachedSecuredResources = new ConcurrentHashMap<>();

    /**
     * 用于缓存每个用户在方法上的权限
     * Key 1 - user id
     * Key 2 - 被拦截的方法
     * Value - permission id
    */
    private Map<String, Map<Method, String>> cachedUserPermissions = null;

    /**
     * 用于缓存每个Permission的定义
     * Key  - permission id
     */
    private Map<String, Permission> cachedPermissions = null;

    @Resource
    private ApplicationSecurityConfig applicationSecurityConfig;

    @Resource
    private DynamicProxy dynamicProxy;

    @Resource
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Resource
    private ApplicationContext applicationContext;

    /**
     * 存放需要安全拦截的资源代理对象
     * Key examples:
     *  1) com.macle.security.sdk.interceptor.PreAuthorityAdvice#execution(* com.macle.study.tmp.controller.HelloController.hello(..))
     *  2) com.macle.security.sdk.interceptor.PreAuthorityAdvice#com.macle.security.sdk.annotation.SecuredResource
     * */
    private Map<String, SecuredAdvisor> cachedSecureddvisorMap = new ConcurrentHashMap<>();

    private Map<String, Method> cachedResourceIdAndMethodMap = new ConcurrentHashMap<>();

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
        MethodInterceptor preAuthorityAdvice = new PreAuthorityAdvice();
        //1. 处理RBAC中心定义的拦截资源
        processSecuredResourcesFromRBAC(preAuthorityAdvice);

        //2. 扫描所有带有@SecuredResource的方法，生成代理对象
        processSecuredResourceAnnotation(preAuthorityAdvice);
    }

    /**
     * 扫描所有带有@SecuredPoint的方法，并生成代理对象
     */
    private void processSecuredResourceAnnotation(MethodInterceptor methodInterceptor) throws Exception {
        for (Method method : findSecuredPointMethods()) {
            //public java.lang.String com.macle.study.springboot.aop.controller.AdvisorController.delete(java.lang.String,java.lang.String,java.lang.String) throws java.lang.Exception
            String expression = "execution(" + method.toString() + ")";
            SecuredPoint securedPointAnnotation = method.getAnnotation(SecuredPoint.class);
            SecuredResource securedResource = new SecuredResource();
            securedResource.setId(method.toString());
            securedResource.setType(ResourceType.METHOD);
            securedResource.setDescription(securedPointAnnotation.resourceName());
            securedResource.setPermissionId(securedPointAnnotation.permissionId());
            securedResource.setAccessControl(securedPointAnnotation.accessControl());
            securedResource.setPreDataAuthority(securedPointAnnotation.preDataAuthority());
            securedResource.setPostDataAuthority(securedPointAnnotation.postDataAuthority());
            this.add(methodInterceptor, expression, null);
            cachedSecuredResources.put(method.toString(), securedResource);
            cachedResourceIdAndMethodMap.put(method.toString(), method);
        }
    }

    /**
     * 加载那些定义在RBAC中的需要安全检查的资源
     * */
    private void processSecuredResourcesFromRBAC(MethodInterceptor methodInterceptor) throws Exception {
        //1. 加载应用的安全拦截点配置
        List<SecuredResource> securedResources = loadSecuredResourcesFromCache(applicationSecurityConfig.getApplicationId());
        if(securedResources == null){
            if(applicationSecurityConfig.getCacheMethod().equals("LOCAL")){
                securedResources = loadSecuredResourcesFromRBAC(applicationSecurityConfig);
            } else {
                //Lock (get distributed lock to avoid multiple instance )
                Object distributedLock = getDistributedLock(applicationSecurityConfig.getApplicationId());
                synchronized(distributedLock){
                    securedResourcesCache = loadSecuredResourcesFromCache(applicationSecurityConfig.getApplicationId());
                    if(securedResourcesCache == null){
                        securedResources = loadSecuredResourcesFromRBAC(applicationSecurityConfig);
                    }
                    //TODO - release lock
                }
            }
        }
        if(securedResources == null){
            //TODO - 如果允许
            throw new SecurityException("No secured resources found for application " + applicationSecurityConfig.getApplicationId());
        }

        //2. 为拦截配置点生成代理对象 TODO
        for (SecuredResource securedResource : securedResources) {
            log.info("resourceId: " + securedResource.getId());
            cachedSecuredResources.put(securedResource.getId(), securedResource);
            Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
            if (securedResource.getType().equals(ResourceType.API)) {
                //API的Pattern
                HandlerMethod handlerMethod = findHandlerMethod(securedResource.getId(), map);//找到API的HandlerMethod
                if (handlerMethod == null) {
                    throw new SecurityException("NO HANDLER FOUND FOR RESOURCE " + securedResource.getId());
                }
                //进一步判断是否是合法的RequestMapping
                RequestMapping requestMapping = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), RequestMapping.class);
                if(requestMapping == null){
                    throw new SecurityException("NO REQUEST MAPPING FOUND FOR RESOURCE " + securedResource.getId());
                }
                //execution(public String com.macle.study.tmp.controller.HelloController.hello(java.lang.String, java.lang.int) throws Exception)
                //重新拼接expression
                String expression = "execution(" + handlerMethod.getMethod().toString() + ")";
                this.add(methodInterceptor, expression, null);
                cachedSecuredResources.put(handlerMethod.getMethod().toString(), securedResource);
                cachedResourceIdAndMethodMap.put(handlerMethod.getMethod().toString(), handlerMethod.getMethod());
            }
            else if((securedResource.getType().equals(ResourceType.METHOD))) {
                //此时的resource id应该直接定义成方法的表达式， 比如下面
                //  com.macle.study.tmp.controller.HelloController.hello()
                //  com.macle.study.tmp.controller.HelloController.hello(java.lang.String)
                //String methodDescription = securedResource.getId().substring(securedResource.getId().indexOf("(") + 1, securedResource.getId().lastIndexOf(")"));
                String className = securedResource.getId().substring(0, securedResource.getId().lastIndexOf("."));
                String methodName = securedResource.getId().substring(securedResource.getId().lastIndexOf(".") + 1, securedResource.getId().indexOf("("));
                Class clazz = Class.forName(className);
                Method[] methods = clazz.getMethods();
                Method bestMatchMethod = null;
                List<Method> similarMethods = new ArrayList<>();
                for (Method method : methods) {
                    if(method.toString().equals(securedResource.getId())){
                        bestMatchMethod = method;
                        break;
                    }
                    else if(method.getName().equals(methodName)){
                        similarMethods.add(method);
                    }
                }
                if(bestMatchMethod != null){
                    cachedResourceIdAndMethodMap.put(bestMatchMethod.toString(), bestMatchMethod);
                    cachedSecuredResources.put(bestMatchMethod.toString(), securedResource);
                    //重新拼接expression
                    String expression = "execution(" + bestMatchMethod.toString() + ")";
                    this.add(methodInterceptor, expression, null);
                }
                else if(similarMethods.size() == 1){
                    cachedResourceIdAndMethodMap.put(similarMethods.get(0).toString(), similarMethods.get(0));
                    cachedSecuredResources.put(similarMethods.get(0).toString(), securedResource);
                    //重新拼接expression
                    String expression = "execution(" + similarMethods.get(0).toString() + ")";
                    this.add(methodInterceptor, expression, null);
                }
                else if(similarMethods.size() == 0){
                    throw new SecurityException("NO METHOD FOUND FOR RESOURCE " + securedResource.getId());
                }
                else if(similarMethods.size() > 0){
                    //TODO - 进一步进行方法参数判断
                    throw new SecurityException("NOT ABLE TO DETERMINE METHOD FOUND FOR RESOURCE " + securedResource.getId());
                }
            }
        }
    }

    /**
     * 通过resourceId查找对应的处理方法
     * @param resourceId like ‘GET /xlob/test/registration/complete/{requestNumber}’
     * */
    private HandlerMethod findHandlerMethod(String resourceId, Map<RequestMappingInfo, HandlerMethod> map) {
        String[] resourceToken = resourceId.split(" ");
        String httpMethod = resourceToken[0];
        RequestMethod requestMethod = RequestMethod.resolve(httpMethod);
        //TODO - Check the requestMethod is null
        String uri = resourceToken[1];
        for(Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            RequestMappingInfo requestMappingInfo = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();
            Set<RequestMethod> handlerMethodSet = requestMappingInfo.getMethodsCondition().getMethods();
            if(!handlerMethodSet.contains(requestMethod)){
                continue;
            }
            if(null != requestMappingInfo.getPathPatternsCondition()) {
                Set<String> patternValues = requestMappingInfo.getPathPatternsCondition().getPatternValues();
                if (patternValues.contains(uri)) {
                    return handlerMethod;
                }
            }
        }
        return null;
    }

    public Permission getPermission(String userId, Method method) {
        return null;
    }

    public SecuredResource getSecuredResource(Method method) {
        return null;
    }

    private List<SecuredResource> loadSecuredResourcesFromCache(String applicationId){
        if(applicationSecurityConfig.getCacheMethod().equals("LOCAL")) {
            return securedResourcesCache;
        }
        else {
            //TODO - GET from REDIS
            return null;
        }
    }

    private List<SecuredResource> loadSecuredResourcesFromRBAC(ApplicationSecurityConfig applicationSecurityConfig){
        return null;
    }

    /**
     * 获取分布式锁，直到获取成功才返回
     * */
    private Object getDistributedLock(String applicationId){
        return null;
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

    /**
     * 从Spring容器中查找所有@SecuredPoint的方法
     * */
    private List<Method> findSecuredPointMethods(){
        List<Method> methods = new ArrayList<Method>();
        for(String beanName : applicationContext.getBeanDefinitionNames()){
            Object bean = applicationContext.getBean(beanName);
            for (int i = 0; i < bean.getClass().getDeclaredMethods().length; i++) {
                Method method = bean.getClass().getDeclaredMethods()[i];
                if(method.isAnnotationPresent(SecuredPoint.class)){
                    methods.add(method);
                }
            }
        }
        return methods;
    }
}