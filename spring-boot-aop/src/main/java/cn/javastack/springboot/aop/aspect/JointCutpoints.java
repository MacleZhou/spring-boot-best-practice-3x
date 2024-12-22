package cn.javastack.springboot.aop.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class JointCutpoints {
    @Pointcut("execution(public cn.javastack.springboot.**.query*(..))")
    private void pc1() {}

    @Pointcut("@annotation(cn.javastack.springboot.aop.annotation.Pack)")
    private void pc2() {}

    // 两个表达式都程序才匹配
    // @Pointcut("pc1() && pc2()")

    // 两个表达式有一个成立即匹配
    @Pointcut("pc1() || pc2()")
    private void pc() {}
}
