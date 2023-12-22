package cn.javastack.jaimini.jim.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义每个类级别的线程池和是否串行或并行
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JoinInMemoryConfig {
    JoinInMemeoryExecutorType executorType() default JoinInMemeoryExecutorType.SERIAL;
    String executorName() default "defaultExecutor";
}
