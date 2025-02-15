package com.macle.security.sdk.interceptor;

import org.springframework.security.authorization.AuthorizationResult;

public class DefaultAuthorizationResult implements AuthorizationResult {

    @Override
    public boolean isGranted() {
        return false;
    }
}
