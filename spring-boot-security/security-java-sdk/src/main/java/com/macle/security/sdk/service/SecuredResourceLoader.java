package com.macle.security.sdk.service;

import com.macle.security.sdk.annotation.SecuredPoint;
import com.macle.security.sdk.model.ResourceType;
import com.macle.security.sdk.model.SecuredResource;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 从本地标识@SecuredPoint或外部定义的资源中查找所有的资源
 * **/

@Slf4j
@Component
public class SecuredResourceLoader {
    /**
     * 用于缓存一个资源的拦截定义
     * Key - 是一个精确匹配的方法描述，通过调用方法的toString()生成，比如
     * public java.lang.String com.macle.study.springboot.aop.controller.AdvisorController.delete(java.lang.String,java.lang.String,java.lang.String) throws java.lang.Exception
     */
    private Map<Method, SecuredResource> cachedSecuredResources = new ConcurrentHashMap<>();

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private ExternalSecuredResourceLoader externalSecuredResourceLoader;

    @Resource
    private RequestMappingHandlerMapping requestMappingHandlerMapping;


    public List<SecuredResource> getSecuredResources() throws Exception {
        List<SecuredResource> securedResources = searchSecuredResourceByAnnotation();
        securedResources.addAll(processSecuredResourcesFromExternal());
        return securedResources;
    }

    public SecuredResource getSecuredResource(Method method) throws Exception {
        String methodString = method.toString();
        return cachedSecuredResources.get(method);
    }

    /**
     * 扫描所有带有@SecuredPoint的方法，并生成代理对象
     */
    private List<SecuredResource> searchSecuredResourceByAnnotation() throws Exception {
        List<SecuredResource> securedResources = new ArrayList<>();
        for (Method method : findSecuredPointMethods()) {
            //public java.lang.String com.macle.study.springboot.aop.controller.AdvisorController.delete(java.lang.String,java.lang.String,java.lang.String) throws java.lang.Exception
            String expression = "execution(" + method.toString() + ")";
            SecuredPoint securedPointAnnotation = method.getAnnotation(SecuredPoint.class);
            SecuredResource securedResource = new SecuredResource();
            securedResource.setExpression(expression);
            securedResource.setId(method.toString());
            securedResource.setType(ResourceType.METHOD);
            securedResource.setDescription(securedPointAnnotation.resourceName());
            securedResource.setPermissionId(securedPointAnnotation.permissionId());
            securedResource.setAccessControl(securedPointAnnotation.accessControl());
            securedResource.setPreDataAuthority(securedPointAnnotation.preDataAuthority());
            securedResource.setPostDataAuthority(securedPointAnnotation.postDataAuthority());
            cachedSecuredResources.put(method, securedResource);
            securedResources.add(securedResource);
        }
        return securedResources;
    }

    /**
     * 加载那些定义在RBAC中的需要安全检查的资源
     * */
    private List<SecuredResource> processSecuredResourcesFromExternal() throws Exception {
        //1. 加载应用的安全拦截点配置
        List<SecuredResource> securedResources = externalSecuredResourceLoader.loadSecuredResources();
        List<SecuredResource> processedSecuredResources = new ArrayList<>();
        if(securedResources == null){
            return processedSecuredResources;
        }

        //2. 为拦截配置点生成代理对象 TODO
        for (SecuredResource securedResource : securedResources) {
            log.info("resourceId: " + securedResource.getId());
            //cachedSecuredResources.put(securedResource.getId(), securedResource);
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
                securedResource.setExpression(expression);
                processedSecuredResources.add(securedResource);
                if(StringUtils.isEmpty(securedResource.getPermissionId())){
                    securedResource.setPermissionId(handlerMethod.getMethod().toString());
                }
                cachedSecuredResources.put(handlerMethod.getMethod(), securedResource);
                //cachedResourceIdAndMethodMap.put(handlerMethod.getMethod().toString(), handlerMethod.getMethod());
            }
            else if((securedResource.getType().equals(ResourceType.METHOD))) {
                //此时的resource id应该直接定义成方法的表达式， 比如下面
                //  com.macle.study.tmp.controller.HelloController.hello()
                //  com.macle.study.tmp.controller.HelloController.hello(java.lang.String)
                //String methodDescription = securedResource.getId().substring(securedResource.getId().indexOf("(") + 1, securedResource.getId().lastIndexOf(")"));
                String className = securedResource.getId().substring(0, securedResource.getId().lastIndexOf("."));
                String methodName = securedResource.getId().substring(securedResource.getId().lastIndexOf(".") + 1, securedResource.getId().indexOf("("));
                Class clazz = Class.forName(className);
                Method[] methods = clazz.getDeclaredMethods();
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
                    //cachedResourceIdAndMethodMap.put(bestMatchMethod.toString(), bestMatchMethod);
                    cachedSecuredResources.put(bestMatchMethod, securedResource);
                    //重新拼接expression
                    String expression = "execution(" + bestMatchMethod.toString() + ")";
                    securedResource.setExpression(expression);
                    processedSecuredResources.add(securedResource);
                }
                else if(similarMethods.size() == 1){
                    //cachedResourceIdAndMethodMap.put(similarMethods.get(0).toString(), similarMethods.get(0));
                    cachedSecuredResources.put(similarMethods.get(0), securedResource);
                    //重新拼接expression
                    String expression = "execution(" + similarMethods.get(0).toString() + ")";
                    securedResource.setExpression(expression);
                    //this.add(methodInterceptor, expression, null);
                    processedSecuredResources.add(securedResource);
                    if(StringUtils.isEmpty(securedResource.getPermissionId())){
                        securedResource.setPermissionId(similarMethods.get(0).toString());
                    }
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
        return processedSecuredResources;
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
            RequestMapping classMapping = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), RequestMapping.class);
            String controllerRequestMappingPath = "";
            if(null != classMapping && classMapping.value().length > 0){
                controllerRequestMappingPath = classMapping.value()[0];
            }
            Set<RequestMethod> handlerMethodSet = requestMappingInfo.getMethodsCondition().getMethods();
            if(!handlerMethodSet.contains(requestMethod)){
                continue;
            }
            if(null != requestMappingInfo.getPathPatternsCondition()) {
                Set<String> patternValues = requestMappingInfo.getPathPatternsCondition().getPatternValues();
                Iterator<String> iterator = patternValues.iterator();
                while(iterator.hasNext()){
                    String pattern = iterator.next();
                    if(pattern.equals(uri)){
                        return handlerMethod;
                    }
                    pattern = controllerRequestMappingPath + pattern;
                    if(pattern.equals(uri)){
                        return handlerMethod;
                    }
                }
            }
        }
        return null;
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
