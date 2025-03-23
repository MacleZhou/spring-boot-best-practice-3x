package com.macle.security.sdk.interceptor;

import com.macle.security.sdk.annotation.ConditionalOnVariableTrue;
import com.macle.security.sdk.config.ApplicationContextHolder;
import com.macle.security.sdk.model.DataPermission;
import com.macle.security.sdk.model.Permission;
import com.macle.security.sdk.model.SecuredResource;
import com.macle.security.sdk.service.SecuredResourceLoader;
import com.macle.security.sdk.service.SecurityProxyService;
import com.macle.security.sdk.service.UserPermissionLoader;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;

@Slf4j
@Component
@ConditionalOnVariableTrue("security.authorization.enabled")
public class AuthorityAdvice implements MethodInterceptor {

    @Resource
    private UserPermissionLoader userPermissionLoader;

    @Resource
    private SecuredResourceLoader securedResourceLoader;


    /**
     * 用于SpEL表达式解析.
     */
    private static final SpelExpressionParser parser = new SpelExpressionParser();

    /**
     * 用于获取方法参数定义名字.
     */
    private static final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    /**
     * 方法增强，检查一个方法是否有调用权限在调用之前
     * TODO - 对于没有数据权限的方法，可以通过userId-方法缓存结果，提升处理速度，比如两小时内不用再次调用
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if(null == securedResourceLoader){
            securedResourceLoader = ApplicationContextHolder.getBean(SecuredResourceLoader.class);
        }
        if(null == userPermissionLoader){
            userPermissionLoader = ApplicationContextHolder.getBean(UserPermissionLoader.class);
        }
        SecuredResource securedResource = securedResourceLoader.getSecuredResource(invocation.getMethod());

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        String userId = user.getUsername();

        Permission permission = userPermissionLoader.loadPermission(userId, securedResource.getPermissionId());

        preAuthorityCheck(invocation, securedResource, permission, userId);

        //调用方法
        Object object = invocation.proceed();

        return object;
    }

    private static void preAuthorityCheck(MethodInvocation invocation, SecuredResource securedResource, Permission permission, String userId){
        accessControllCheck(invocation, securedResource, permission, userId);

        if(securedResource.isPreDataAuthority()){
            preDataAuthorityCheck(invocation, permission, userId, securedResource);
        }
    }

    private static void postAuthorityCheck(MethodInvocation invocation, Permission permission, String userId, Object object){
        //TODO
    }

    private static void accessControllCheck(MethodInvocation invocation, SecuredResource securedResource, Permission permission, String userId) {
        log.debug("PreSecuredInterceptor invoked on method {}.{}", invocation.getMethod().getDeclaringClass(), invocation.getMethod().getName());
        if (permission == null) {
            throw new AuthorizationDeniedException(userId + ", NO AUTHORITY GRANTED FOR YOU ON " + securedResource.getDescription(), new DefaultAuthorizationResult());
        }

        if(securedResource.isAccessControl() && !permission.isAllowAccess()){
            throw new AuthorizationDeniedException(userId + ", NO ACCESS AUTHORITY GRANTED FOR YOU ON " + securedResource.getDescription(), new DefaultAuthorizationResult());
        }
    }

    private static void preDataAuthorityCheck(MethodInvocation invocation, Permission permission, String userId, SecuredResource securedResource) {
        if(CollectionUtils.isEmpty(permission.getPreDataPermissions())) {
            throw new AuthorizationDeniedException(userId + ", NO Pre DATA AUTHORITY GRANTED FOR YOU ON " + securedResource.getDescription(), new DefaultAuthorizationResult());
        }
        //Check pre data permission
        Method method = invocation.getMethod();
        // 使用Spring的DefaultParameterNameDiscoverer获取方法形参名数组
        String[] paramNames = nameDiscoverer.getParameterNames(method);
        DataPermission passedDataPermission = null;
        //TODO - Change to concurrent
        for(DataPermission dataPermission : permission.getPreDataPermissions()) {
            // 解析过后的Spring表达式对象
            Expression expression = parser.parseExpression(dataPermission.getExpression());
            // Spring的表达式上下文对象
            EvaluationContext context = new StandardEvaluationContext();
            // 通过joinPoint获取被注解方法的形参
            Object[] args = invocation.getArguments();
            // 给上下文赋值
            for (int i = 0; i < args.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
            boolean pass = expression.getValue(context, Boolean.class);
            if (pass) {
                //有一个通过则通过
                passedDataPermission = dataPermission;
                break;
            }
        }
        if(null == passedDataPermission){
            throw new AuthorizationDeniedException(userId + ", NO Pre DATA AUTHORITY ALLOWED YOU ON " + securedResource.getDescription(), new DefaultAuthorizationResult());
        }
    }
}
