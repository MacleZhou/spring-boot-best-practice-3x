package cn.javastack.springboot.aop.inteceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class TwoInteceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object object = invocation.proceed();

        return object + "-增强Two";
    }
}
