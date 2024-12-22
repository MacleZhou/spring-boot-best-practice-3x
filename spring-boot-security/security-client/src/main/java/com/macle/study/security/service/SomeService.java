package com.macle.study.security.service;

/**
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PrePostAdviceReactiveMethodInterceptor;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdviceVoter;
*/
 import org.springframework.stereotype.Service;

@Service
public class SomeService {

    //@PreAuthorize("aaa")
    public String preAuthorizeService(){
        return "preAuthorizeService";
    }

    public void hasRoleService(){}

}
