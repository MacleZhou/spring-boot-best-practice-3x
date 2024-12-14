package com.macle.study;

import com.macle.study.m1.config.M1Config;
import com.macle.study.m2.config.M2Config;
import com.macle.study.parent.config.ParentConfig;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;


//@SpringBootApplication
public class SpringbootParentChildApplication {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder() ;
        builder
                // 配置父容器的配置类（这一步内部就将当前容器设置为非web容器）
                .parent(ParentConfig.class)
                // 配置子容器对应的配置及web应用
                .child(M1Config.class).web(WebApplicationType.SERVLET)
                // 配置子容器（与上面的child相当于兄弟容器），他们都有共同的父容器
                // 在这一步将会创建父容器及上面child子容器
                .sibling(M2Config.class).web(WebApplicationType.SERVLET)
                // 而这里则会创建sibling设置子容器
                .run(args) ;
    }
}
