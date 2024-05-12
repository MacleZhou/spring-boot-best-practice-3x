package com.macle.study.dynamicapi.controller;

import com.macle.study.dynamicapi.base.ApiRegistrar;
import com.macle.study.dynamicapi.base.RequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ApiController<T> {
    @Autowired
    private ApiRegistrar apiRegistrar;

    @Autowired
    private RequestHandler requestHandler;

    @GetMapping("/registerApis")
    public void registerApis() {
        apiRegistrar.registerApis();
    }

    @PostMapping("/{apiName}")
    public ResponseEntity<?> handleRequest(@PathVariable String apiName, @RequestBody T request) throws Exception {
        String apiPath = "/" + apiName.toLowerCase(); // 根据请求的路径确定接口路径
        System.out.println(apiPath);
        return requestHandler.handleRequest(apiPath, request); // 将请求交给 RequestHandler 处理
    }
}