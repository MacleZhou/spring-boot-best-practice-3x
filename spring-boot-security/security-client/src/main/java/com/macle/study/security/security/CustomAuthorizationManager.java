package com.macle.study.security.security;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;

public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        //获取API的方法(PUT/POST/GET/DELETE/...)和路径
        String method = context.getRequest().getMethod();
        String path = context.getRequest().getRequestURI();

        //从数据库中获取当前API需要的权限
        String allowedRoles = getPermissionFromDatabase(method, path);
        if (allowedRoles != null) {
            Authentication auth = authentication.get();
            return new AuthorizationDecision(auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(authority -> allowedRoles.contains(authority)));
        }
        return new AuthorizationDecision(false);
    }

    private String getPermissionFromDatabase(String httpMethod, String path) {
        /**
         * 从数据库中查询权限，权限表的定义如下
         *    id  |  method | path                       | allowedRoles                         | deniedRoles
         *    1   |  GET    | /api/data                  | ROLE_READ, ROLE_WRITE                | ANOMOS
         *    2   |  PUT    | /api/data                  | ROLE_WRITE                           | ANOMOS
         * */
        if("GET".equals(httpMethod) && path.equals("/api/api-security/user")) {
            return "ROLE_USER, ROLE_ADMIN";
        } else if("PUT".equals(httpMethod) && path.equals("/api/api-security/user")) {
            return "ROLE_ADMIN";
        }
        return null;
    }
}
