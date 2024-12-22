package cn.javastack.springboot.aop.controller;

import cn.javastack.springboot.aop.annotation.XdxAnnotation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {
    /**
    * 用了测试动态AOP的功能
    * */
    @GetMapping("/fun")
    @XdxAnnotation
    public String hello() {
        return "hello world";
    }
}
