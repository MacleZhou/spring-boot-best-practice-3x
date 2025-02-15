package cn.javastack.springboot.aop.controller;

import cn.javastack.springboot.aop.annotation.XdxAnnotation;
import jakarta.annotation.Resource;
import org.springframework.scheduling.config.AnnotationDrivenBeanDefinitionParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;

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

    @GetMapping("/fun2")
    @XdxAnnotation
    public void hello2() {
    }

    @GetMapping("/fun3")
    @XdxAnnotation
    public void hello3(String a, String b,  String c) throws RuntimeException {
    }

    @GetMapping("/fun4")
    @XdxAnnotation
    public String hello4() throws RuntimeException {
        return "hello world";
    }
}
