package cn.javastack.springboot.aop.plugin;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class XdxAdvisor extends AbstractPointcutAdvisor {
    private final Advice advice;
    private final Pointcut pointcut;
    public XdxAdvisor(Class<? extends Annotation> annotation, MethodInterceptor methodInterceptor) {
        this.advice = methodInterceptor;
        this.pointcut = buildPointcut(annotation);
    }

    public XdxAdvisor(String expression, MethodInterceptor methodInterceptor) {
        this.advice = methodInterceptor;
        this.pointcut = buildPointcut(expression);
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    /**
     * Copy from @Async 构建pointcut的代码
     * */
    private Pointcut buildPointcut(Class<? extends Annotation> annotation) {
        Set<Class<? extends Annotation>> annotationSet = new LinkedHashSet<>();
        annotationSet.add(annotation);
        ComposablePointcut result = null;
        AnnotationMatchingPointcut mpc;
        for(Iterator iterator = annotationSet.iterator(); iterator.hasNext(); result = result.union(mpc)) {
            Class<? extends Annotation> myAnnotationType = (Class<? extends Annotation>) iterator.next();
            Pointcut cpc = new AnnotationMatchingPointcut(myAnnotationType, true);
            mpc = new AnnotationMatchingPointcut((Class<? extends Annotation>) null, myAnnotationType, true);
            if(result == null){
                result = new ComposablePointcut(cpc);
            } else {
                result.union(cpc);
            }
        }
        return (Pointcut) (result != null ? result : Pointcut.TRUE);
    }

    private Pointcut buildPointcut(String expression) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);
        return pointcut;
    }

}
