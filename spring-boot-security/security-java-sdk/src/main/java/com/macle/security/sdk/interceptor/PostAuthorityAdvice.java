package com.macle.security.sdk.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

@Slf4j
public class PostAuthorityAdvice implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object object = invocation.proceed();
        //TODO 方法调用后，检查是否数据权限处理，包括数据过滤或者mask
        log.debug("PostAuthorityInterceptor invoked.");
        return object;
    }
}