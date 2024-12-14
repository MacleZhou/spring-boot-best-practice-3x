package com.macle.study.parent.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


// 测试接口
@RestController
@RequestMapping("/p")
public class ParentController {

    private final ParentService parentService ;

    public ParentController(ParentService parentService) {
        this.parentService = parentService ;
    }

    @GetMapping("/index")
    public Object parentIndex() {
        return "module parent - " + this.parentService.query() ;
    }
}