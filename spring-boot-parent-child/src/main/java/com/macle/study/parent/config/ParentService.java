package com.macle.study.parent.config;

import org.springframework.stereotype.Service;

@Service
public class ParentService {

    public String query() {
        return "我是父模块内容" ;
    }
}