package com.et.api.version.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class HelloWorldController {
    //@GetMapping(params = "version=1")
    @GetMapping(value = "/header-accept", produces = "application/vnd.company.app-v1+json")
    //@GetMapping(headers = "API-Version=1")
    public Map<String, Object> showHelloWorldByHeaderAcceptOne(){
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "HelloWorld1-accept");
        return map;
    }
    //@GetMapping(params = "version=2")
    @GetMapping(value = "/header-accept", produces = "application/vnd.company.app-v2+json")
    //@GetMapping(headers = "API-Version=2")
    public Map<String, Object> showHelloWorldByHeaderAcceptTwo(){
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "HelloWorld2-accept");
        return map;
    }

    @GetMapping(value="/request-parameter", params = "version=1")
    public Map<String, Object> showHelloWorldByRequestParameterOne(){
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "HelloWorld1-request-parameter");
        return map;
    }

    @GetMapping(value="/request-parameter", params = "version=2")
    public Map<String, Object> showHelloWorldByRequestParameterTwo(){
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "HelloWorld2-request-parameter");
        return map;
    }

    @GetMapping(value="/header-parameter", headers = "API-Version=1")
    public Map<String, Object> showHelloWorldByHeaderParameterOne(){
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "HelloWorld1-header-parameter");
        return map;
    }

    @GetMapping(value="/header-parameter", headers = "API-Version=2")
    public Map<String, Object> showHelloWorldByHeaderParameterTwo(){
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "HelloWorld2-header-parameter");
        return map;
    }
}