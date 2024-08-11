package com.macle.study.deployment;

import io.swagger.v3.oas.models.OpenAPI;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.impl.internal.concurrent.ConcurrentHashMap;
import org.springdoc.api.AbstractOpenApiResource;
import org.springdoc.core.providers.SpringDocProviders;
import org.springdoc.core.providers.SpringWebProvider;
import org.springdoc.core.service.OpenAPIService;
import org.springdoc.webmvc.api.MultipleOpenApiResource;
import org.springdoc.webmvc.api.OpenApiResource;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.MethodIntrospector;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author gx
 * @see <a href="https://www.jianshu.com/u/aba665c4151f">简书TinyThing</a>
 * @since 2024/7/3 9:18
 */
//@Component
@RequiredArgsConstructor
@Slf4j
public class DynamicControllerUtils {

    private final ApplicationContext applicationContext;
    private final RequestMappingHandlerMapping handlerMapping;
    private final SysScriptService scriptService;
    private final MultipleOpenApiResource multipleOpenApiResource;
    private final SpringDocProviders springDocProviders;

    private AnnotationConfigApplicationContext dynamicContext;
    private static final Map<String, Collection<RequestMappingInfo>> MAPPINGS = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        dynamicContext = new AnnotationConfigApplicationContext();
        dynamicContext.setParent(applicationContext);
        dynamicContext.refresh();
    }

    /**
     * 初始化动态controller
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initDynamicController() {
        log.info("初始化动态controller");
        //List<SysScript> list = scriptService.lambdaQuery().likeRight(SysScript::getType, "controller:").list();
        List<SysScript> list = new ArrayList<>();
        for (SysScript sysScript : list) {
            try {
                registerController(sysScript);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void refresh(String scriptType) {
        String script = scriptService.getByType(scriptType);

        try {
            //删除旧的bean
            dynamicContext.getDefaultListableBeanFactory().removeBeanDefinition(scriptType);

            //注册新的bean
            //Class<Object> type = GroovyUtils.loadClass(script);
            Class<?> type = this.getClass().getClassLoader().loadClass(script);

            dynamicContext.registerBean(scriptType, type);
            Object bean = dynamicContext.getBean(scriptType);

            //注销原有的controller
            unregister(scriptType);

            //重新注册controller
            detectHandlerMethods(bean, scriptType);

            //刷新open api缓存，包括刷新openAPIService内部的mappingsMap和springWebProvider内部的handlerMethods
            OpenAPIService openAPIService = getOpenApiService();
            openAPIService.addMappings(Map.of(bean.toString(), bean));
            Optional<SpringWebProvider> springWebProvider = springDocProviders.getSpringWebProvider();
            if (springWebProvider.isPresent()) {
                Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
                springWebProvider.get().getHandlerMethods().putAll(handlerMethods);
            }

            // 反射获取 openAPIService中的缓存
            Field cachedOpenAPIField = OpenAPIService.class.getDeclaredField("cachedOpenAPI");
            ReflectionUtils.makeAccessible(cachedOpenAPIField);
            Map<String, OpenAPI> cache = (Map<String, OpenAPI>) cachedOpenAPIField.get(openAPIService);
            cache.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 注销原有的controller接口
     *
     * @param type type
     */
    private void unregister(String type) {
        Collection<RequestMappingInfo> mappings = MAPPINGS.remove(type);
        if (CollectionUtils.isEmpty(mappings)) {
            log.warn("未找到{}相关的映射", type);
            return;
        }

        for (RequestMappingInfo mapping : mappings) {
            handlerMapping.unregisterMapping(mapping);
            log.info("{} HTTP接口{}注销成功", type, mapping);
        }
    }

    /**
     * 注册controller到mapping，并添加到open api
     *
     * @param script 脚本
     */
    private void registerController(SysScript script) throws ClassNotFoundException {
        String s = scriptService.getScript(script);
        //Class<Object> type = GroovyUtils.loadClass(s);

        Class<?> type = this.getClass().getClassLoader().loadClass(s);


        dynamicContext.registerBean(script.getType(), type);
        Object bean = dynamicContext.getBean(type);

        detectHandlerMethods(bean, script.getType());

        OpenAPIService openApiService = getOpenApiService();
        openApiService.addMappings(Map.of(bean.toString(), bean));
    }


    private OpenAPIService getOpenApiService() {
        try {
            //反射获取openApiResource
            Method getOpenApiResource = MultipleOpenApiResource.class.getDeclaredMethod("getOpenApiResourceOrThrow", String.class);
            ReflectionUtils.makeAccessible(getOpenApiResource);
            OpenApiResource openApiResource = (OpenApiResource) getOpenApiResource.invoke(multipleOpenApiResource, "dynamic");

            // 反射获取 openAPIService
            Field openAPIServiceField = AbstractOpenApiResource.class.getDeclaredField("openAPIService");
            ReflectionUtils.makeAccessible(openAPIServiceField);
            return (OpenAPIService) openAPIServiceField.get(openApiResource);
        } catch (Exception e) {
            System.out.println("获取反射获取openApiResource和openApiService失败");
        }
        return null;
    }

    /**
     * Look for handler methods in the specified handler bean.
     *
     * @param handler either a bean name or an actual handler instance
     * @param name
     */
    private void detectHandlerMethods(Object handler, String name) {
        Class<?> handlerType = handler.getClass();

        Class<?> userType = ClassUtils.getUserClass(handlerType);
        Map<Method, RequestMappingInfo> methods = MethodIntrospector.selectMethods(userType,
                (MethodIntrospector.MetadataLookup<RequestMappingInfo>) method -> getMappingForMethod(method, userType));

        MAPPINGS.put(name, methods.values());
        methods.forEach((method, mapping) -> {
            Method invocableMethod = AopUtils.selectInvocableMethod(method, userType);
            handlerMapping.registerMapping(mapping, handler, invocableMethod);
            log.info("接口{}注册成功", mapping);
        });
    }


    private RequestMappingInfo getMappingForMethod(Method method, Class<?> userType) {
        try {
            Method getMappingForMethod = AbstractHandlerMethodMapping.class.getDeclaredMethod("getMappingForMethod", Method.class, Class.class);
            ReflectionUtils.makeAccessible(getMappingForMethod);
            return (RequestMappingInfo) getMappingForMethod.invoke(handlerMapping, method, userType);
        } catch (Exception e) {
            System.out.println("Invalid mapping on handler class [" + userType.getName() + "]: " + method);
        }
        return null;
    }
}