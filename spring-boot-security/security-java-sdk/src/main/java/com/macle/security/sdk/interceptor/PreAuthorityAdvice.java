package com.macle.security.sdk.interceptor;

import com.macle.security.sdk.model.Permission;
import com.macle.security.sdk.model.SecuredResource;
import com.macle.security.sdk.service.SecurityService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.util.CollectionUtils;

@Slf4j
public class PreAuthorityAdvice implements MethodInterceptor {

    @Resource
    private SecurityContext securityContext;

    @Resource
    private SecurityService securityService;

    /**
     * 方法增强，检查一个方法是否有调用权限在调用之前
     * TODO - 对于没有数据权限的方法，可以通过userId-方法缓存结果，提升处理速度，比如两小时内不用再次调用
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        SecuredResource securedResource = securityService.getSecuredResource(invocation.getMethod());

        Authentication authentication = securityContext.getAuthentication();
        String userId = (String)authentication.getPrincipal();

        Permission permission = securityService.getPermission(userId, invocation.getMethod());

        //TODO 检查是否有权限调用此功能
        log.debug("PreSecuredInterceptor invoked on method {}.{}", invocation.getMethod().getDeclaringClass(), invocation.getMethod().getName());

        if (permission == null) {
            throw new AuthorizationDeniedException(userId + ", NO AUTHORITY GRANTED FOR YOU ON " + securedResource.getDescription(), new DefaultAuthorizationResult());
        }

        if(securedResource.isAccessControl() && !permission.isAllowAccess()){
            throw new AuthorizationDeniedException(userId + ", NO ACCESS AUTHORITY GRANTED FOR YOU ON " + securedResource.getDescription(), new DefaultAuthorizationResult());
        }

        if(securedResource.isPreDataAuthority()){
            if(CollectionUtils.isEmpty(permission.getPreDataPermissions())) {
                throw new AuthorizationDeniedException(userId + ", NO Pre DATA AUTHORITY GRANTED FOR YOU ON " + securedResource.getDescription(), new DefaultAuthorizationResult());
            } else {
                //Check pre data permission
            }
        }

        Object object = invocation.proceed();

        return object;
    }
}
