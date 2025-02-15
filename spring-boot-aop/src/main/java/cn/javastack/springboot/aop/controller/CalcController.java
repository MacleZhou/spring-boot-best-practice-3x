package cn.javastack.springboot.aop.controller;

import cn.javastack.springboot.aop.annotation.XdxAnnotation;
import jakarta.annotation.Resource;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.*;
import cn.javastack.springboot.aop.service.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 微信公众号：Java技术栈
 */
@RequiredArgsConstructor
@RestController
public class CalcController {

    private final CalcService calcService;

    @Resource
    private RequestMappingHandlerMapping handlerMapping;

    @Resource
    private ApplicationContext applicationContext;

    @GetMapping("/calc/divide")
    public int divide(@RequestParam("param1") int param1,
                      @RequestParam("param2") int param2) {

        Map<RequestMappingInfo, HandlerMethod> map = handlerMapping.getHandlerMethods();

        System.out.println("\n");
        System.out.println("\nAAAAA-0");
        map.forEach((mappingInfo, handlerMethod) -> {
            String httpMethod = null;
            Set<RequestMethod> methods = mappingInfo.getMethodsCondition().getMethods();
            for (RequestMethod requestMethod : methods) {
                httpMethod = requestMethod.name();
            }

            String uri = null;
            for (String urlPattern : mappingInfo.getPathPatternsCondition()
                    .getPatternValues()) {
                uri = urlPattern;
            }

            System.out.println(httpMethod + " " + uri + "#" + handlerMethod.getMethod().toString());
            /*
            RequestMapping requestMapping = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), RequestMapping.class);
            if (requestMapping != null) {
                String[] paths = requestMapping.value();
                Method method = handlerMethod.getMethod();
                String methodName = method.getName();

                System.out.println("URI: " + paths[0] + ", Method: " + method.toString());
            }*/
        });
        System.out.println("\nAAAAA-1");
        List<Method> methods = new ArrayList<Method>();
        for(String beanName : applicationContext.getBeanDefinitionNames()){
            //System.out.println("\nbeanName: " + beanName);
            Object bean = applicationContext.getBean(beanName);
            for (int i = 0; i < bean.getClass().getDeclaredMethods().length; i++) {
                Method method = bean.getClass().getDeclaredMethods()[i];
                if(method.isAnnotationPresent(XdxAnnotation.class)){
                    System.out.println(method.toGenericString());
                }
            }
        }
        System.out.println("\nAAAAA-2");
        return calcService.divide(param1, param2);
    }

    @GetMapping("/calc/divide2/{param1}/{param2}")
    public int divide2(@PathVariable("param1") int param1,
                      @PathVariable("param2") int param2) {
        return calcService.divide(param1, param2);
    }

    @GetMapping("/calc/divide3/{param1}?param2={param2}")
    public int divide3(@PathVariable("param1") int param1,
                       @PathParam("param2") int param2) {
        return calcService.divide(param1, param2);
    }
}
