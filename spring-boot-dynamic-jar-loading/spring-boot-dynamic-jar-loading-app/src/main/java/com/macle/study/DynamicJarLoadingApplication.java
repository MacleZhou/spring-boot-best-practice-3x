package com.macle.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 演示Springboot动态加载Jar包
 * http://localhost:8080/calculate/5/2          will return 404
 * http://localhost:8080/load-jar               will return below
 * LOAD CHECK:::; BEFORE = true; AFTER = false; AFTER1 = false; AFTER2 = false; AFTER3 = false; AFTER4 = true
 * http://localhost:8080/calculate/5/2          will return 7
 * http://localhost:8080/add/3/2                will return 5
 * */


@SpringBootApplication
public class DynamicJarLoadingApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DynamicJarLoadingApplication.class, args);
    }
}
