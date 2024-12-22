package cn.javastack.springboot.aop.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class CommonCutpoints {

    @Pointcut("execution(* cn.javastack.*.*(..))")
    public void log() {}

    @Pointcut("@annotation(cn.javastack.springboot.aop.annotation.Pack)")
    public void auth() {}
}