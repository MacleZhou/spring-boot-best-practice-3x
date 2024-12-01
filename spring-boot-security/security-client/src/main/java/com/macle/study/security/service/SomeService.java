package com.macle.study.security.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class SomeService {

    public String preAuthorizeService(){
        return "preAuthorizeService";
    }

}
