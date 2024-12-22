package cn.javastack.springboot.aop.controller;

import cn.javastack.springboot.aop.annotation.XdxAnnotation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello2")
@XdxAnnotation
public class Hello2Controller {
    /**
    * 用了测试动态AOP的功能
    * */
    @GetMapping("/fun1")
    public String hello() {
        return "hello world";
    }

    @GetMapping("/fun2")
    public String hello2() {
        return "hello world";
    }
}
