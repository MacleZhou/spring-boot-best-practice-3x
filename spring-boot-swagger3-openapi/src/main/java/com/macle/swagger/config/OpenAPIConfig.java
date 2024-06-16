package com.macle.swagger.config;

import com.fasterxml.classmate.TypeResolver;
import com.macle.swagger.annotations.Admin;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.scanners.ApiDescriptionLookup;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("测试 title")
                        .description("SpringBoot3 集成 Swagger3")
                        .version("v1"))
                .externalDocs(new ExternalDocumentation()
                        .description("项目API文档")
                        .url("/"));
    }

    @Bean
    public GroupedOpenApi tempApi() {
        return GroupedOpenApi.builder()
                .group("temp")
                .pathsToMatch("/temp/**")
                //.addOpenApiCustomizer()
                //.addOperationCustomizer(OperationCustomizer)
                .build();
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("springshop-public")
                .pathsToMatch("/public/**")
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("springshop-admin")
                .pathsToMatch("/admin/**")
                .addOpenApiMethodFilter(method -> method.isAnnotationPresent(Admin.class))
                .build();
    }

    @Bean
    public TypeResolver typeResolver(){
        return new TypeResolver();
    }

    @Bean
    public ApiDescriptionLookup apiDescriptionLookup(){
        return new ApiDescriptionLookup();
    }

    @Bean
    public DocumentationCache documentationCache(){
        return new DocumentationCache();
    }
}