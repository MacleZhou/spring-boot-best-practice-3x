package com.macle.study.m1.config;

import com.macle.study.parent.config.ParentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/m")
public class M1Controller {

    private final ParentService parentService ;
    public M1Controller(ParentService parentService) {
        this.parentService = parentService ;
    }

    @GetMapping("/index")
    public Object m1() {
        return "module 01 -" + this.parentService.query() ;
    }
}
