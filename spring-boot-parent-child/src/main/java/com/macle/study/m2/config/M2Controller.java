package com.macle.study.m2.config;

import com.macle.study.parent.config.ParentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 测试接口
@RestController
@RequestMapping("/m")
public class M2Controller {

    private final ParentService parentService ;

    public M2Controller(ParentService parentService) {
        this.parentService = parentService ;
    }

    @GetMapping("/index")
    public Object m2() {
        return "module 02 - " + this.parentService.query() ;
    }
}