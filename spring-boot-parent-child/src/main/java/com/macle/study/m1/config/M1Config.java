package com.macle.study.m1.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {"com.macle.study.m1"})
@PropertySource({"classpath:m1.properties"})
@EnableAutoConfiguration
public class M1Config {
}