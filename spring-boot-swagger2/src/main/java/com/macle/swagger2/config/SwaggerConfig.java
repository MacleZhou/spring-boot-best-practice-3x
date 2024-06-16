package com.macle.swagger2.config;

import com.macle.swagger2.annotations.FlowType;
import com.macle.swagger2.annotations.FlowTypes;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
//@EnableKnife4j
@EnableSwagger2WebMvc
public class SwaggerConfig {

    private DefaultListableBeanFactory context;
    private RequestMappingHandlerMapping handlerMapping;

    public SwaggerConfig(DefaultListableBeanFactory context,
                         RequestMappingHandlerMapping handlerMapping) {
        this.context = context;
        this.handlerMapping = handlerMapping;
        dynamicCreate();
    }

    private void dynamicCreate() {
        // 分组
        Set<String> groupNames = getGroupName();
        // 根据分好的组,循环创建配置类并添加到容器中
        groupNames.forEach(item -> {
            Docket docket = new Docket(DocumentationType.SWAGGER_2)
                    .groupName(item)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("cn.com.kanq.dynamic")) // 确保生成的Docket扫不到任何可以生成API文档的注解
                    .paths(PathSelectors.any())
                    .build();
            // 手动将配置类注入到spring bean容器中
            context.registerSingleton("dynamicDocket" + item, docket);
        });
    }

    private Set<String> getGroupName() {
        HashSet<String> set = new HashSet<>();
        Map<RequestMappingInfo, HandlerMethod> mappingHandlerMethods = handlerMapping
                .getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> map : mappingHandlerMethods.entrySet()) {
            HandlerMethod method = map.getValue();
            FlowTypes gisServiceTypes = method.getMethod().getAnnotation(FlowTypes.class);
            if (null != gisServiceTypes) {
                FlowType[] value = gisServiceTypes.value();
                for (FlowType gisServiceType : value) {
                    set.add(gisServiceType.value());
                }
            }
        }
        return set;
    }
}