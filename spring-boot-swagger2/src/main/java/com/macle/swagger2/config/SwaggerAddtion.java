package com.macle.swagger2.config;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Sets;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.Operation;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class SwaggerAddtion implements ApiListingScannerPlugin {

    @Override
    public List<ApiDescription> apply(DocumentationContext documentationContext) {

        Operation usernamePasswordOperation = new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.POST)
                .summary("用户名密码登录")
                .notes("username/password登录")
                // 接收参数格式
                .consumes(Sets.newHashSet(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                // 返回参数格式
                .produces(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))
                .tags(Sets.newHashSet("登录"))
                .parameters(Arrays.asList(
                        new ParameterBuilder()
                                .description("用户名")
                                .type(new TypeResolver().resolve(String.class))
                                .name("username")
                                .defaultValue("admin")
                                .parameterType("query")
                                .parameterAccess("access")
                                .required(true)
                                .modelRef(new ModelRef("string"))
                                .build(),
                        new ParameterBuilder()
                                .description("密码")
                                .type(new TypeResolver().resolve(String.class))
                                .name("password")
                                .defaultValue("123456")
                                .parameterType("query")
                                .parameterAccess("access")
                                .required(true)
                                .modelRef(new ModelRef("string"))
                                .build(),
                        new ParameterBuilder()
                                .description("验证码")
                                .type(new TypeResolver().resolve(String.class))
                                .name("code")
                                .parameterType("query")
                                .parameterAccess("access")
                                .required(true)
                                .modelRef(new ModelRef("string"))
                                .build()
                ))
                .responseMessages(Collections.singleton(
                        new ResponseMessageBuilder().code(200).message("请求成功")
                                .responseModel(new ModelRef(
                                        "com.macle.swagger.controller.dto.SwaggerApiModel")
                                ).build()))
                .build();

        Operation logoutOperation = new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.GET)
                .summary("登出")
                .notes("登出")
                .tags(Sets.newHashSet("登出"))
                .responseMessages(Collections.singleton(
                        new ResponseMessageBuilder().code(200).message("请求成功")
                                .responseModel(new ModelRef(
                                        "com.macle.swagger.controller.dto.SwaggerApiModel")
                                ).build()))
                .build();

        ApiDescription loginApiDescription = new ApiDescription("temp", "/temp/login", "登录接口", "description login",
                Collections.singletonList(usernamePasswordOperation), false);
        ApiDescription logoutApiDescription = new ApiDescription("temp", "/temp/logout", "登出接口", "description logout",
                Collections.singletonList(logoutOperation), false);


        ArrayList<ApiDescription> list = new ArrayList<ApiDescription>(
                Arrays.asList(
                        new ApiDescription(
                                "temp",  //group name
                                "/temp/token", //path,
                                "summary",
                                "description",
                                Arrays.asList(
                                        new OperationBuilder(
                                                new CachingOperationNameGenerator())
                                                .method(HttpMethod.POST)//http请求类型
                                                .produces(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))
                                                .summary("获取token")
                                                .notes("获取token")//方法描述
                                                .tags(Sets.newHashSet("Token"))//归类标签
                                                .parameters(
                                                        Arrays.asList(
                                                                new ParameterBuilder()
                                                                        .description("oauth2鉴权方式，如password")//参数描述
                                                                        .type(new TypeResolver().resolve(String.class))//参数数据类型
                                                                        .name("grant_type")//参数名称
                                                                        .defaultValue("password")//参数默认值
                                                                        .parameterType("query")//参数类型
                                                                        .parameterAccess("access")
                                                                        .required(true)//是否必填
                                                                        .modelRef(new ModelRef("string")) //参数数据类型
                                                                        .build(),
                                                                new ParameterBuilder()
                                                                        .description("用户名")
                                                                        .type(new TypeResolver().resolve(String.class))
                                                                        .name("username")
                                                                        .parameterType("query")
                                                                        .parameterAccess("access")
                                                                        .required(true)
                                                                        .modelRef(new ModelRef("string")) //<5>
                                                                        .build(),
                                                                new ParameterBuilder()
                                                                        .description("密码")
                                                                        .type(new TypeResolver().resolve(String.class))
                                                                        .name("password")
                                                                        .parameterType("query")
                                                                        .parameterAccess("access")
                                                                        .required(true)
                                                                        .modelRef(new ModelRef("string")) //<5>
                                                                        .build()
                                                        ))
                                                .build()),
                                false)));
        list.add(loginApiDescription);
        list.add(logoutApiDescription);
        return list;
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return DocumentationType.OAS_30.equals(documentationType);
    }
}