package cn.javastack.springboot.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class LogAspect {

    @Around("cn.javastack.springboot.aop.aspect.CommonCutpoints.log()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("before...") ;
        Object ret = pjp.proceed() ;
        System.out.println("after...") ;
        return ret ;
    }
}