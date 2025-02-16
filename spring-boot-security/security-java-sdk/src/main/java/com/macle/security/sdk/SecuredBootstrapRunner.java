package com.macle.security.sdk;

import com.macle.security.sdk.service.SecurityProxyService;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SecuredBootstrapRunner implements CommandLineRunner {
    @Resource
    private SecurityProxyService securityProxyService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Spring Boot application started.");
        // 执行需要的逻辑
        securityProxyService.initSecuredResources();
    }
}
