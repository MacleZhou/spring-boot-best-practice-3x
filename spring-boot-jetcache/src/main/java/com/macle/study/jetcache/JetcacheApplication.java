package com.macle.study.jetcache;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = "com.macle.study")
@SpringBootApplication
public class JetcacheApplication {
    public static void main(String[] args) {
        SpringApplication.run(JetcacheApplication.class, args);
    }
}
