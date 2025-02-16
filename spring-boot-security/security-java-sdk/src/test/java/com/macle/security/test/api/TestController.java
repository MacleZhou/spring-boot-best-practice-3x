package com.macle.security.test.api;

import com.macle.security.sdk.annotation.SecuredPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {
    /**
     * 此方法需要权限控制，通过用户的permissionId获取的权限决定是否能访问，当同时满足以下条件才能调用此方法
     * 1. 用户拥有一个permissionId=test-api-by-annotation的权限
     * 2. 权限中的accessControl=true
     * */
    @SecuredPoint(resourceName = "test-api-by-annotation", permissionId = "test-api-by-annotation", accessControl = true)
    @GetMapping("/test-api-by-annotation")
    public String testApiByAnnotation(){
        return "test-api-by-annotation";
    }

    @GetMapping("/test-api-by-remote-resource")
    public String testApiByRemote(){
        return "test-api-by-annotation";
    }
}
