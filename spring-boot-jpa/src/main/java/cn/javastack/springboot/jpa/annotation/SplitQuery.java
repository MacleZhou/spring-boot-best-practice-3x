package cn.javastack.springboot.jpa.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SplitQuery {
    /**线程池bean名称;类型必须是Executor*/
    String executorName() default "" ;

    /**批处理大小*/
    int batchSize() default 100 ;

    /**返回值结果处理器beanName;类型必须是ResultHandler*/
    String handlerName() default "" ;
}