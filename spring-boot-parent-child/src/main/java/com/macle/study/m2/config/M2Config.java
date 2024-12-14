package com.macle.study.m2.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {"com.macle.study.m2"})
@PropertySource({"classpath:m2.properties"})
@EnableAutoConfiguration
public class M2Config {
}