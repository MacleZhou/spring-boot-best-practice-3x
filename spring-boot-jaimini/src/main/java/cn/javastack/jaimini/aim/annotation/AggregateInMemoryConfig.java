package cn.javastack.jaimini.aim.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is on class level to define is serial or parallel execute aggregate
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AggregateInMemoryConfig {
    AggregateInMemoryExecutorType executorType() default AggregateInMemoryExecutorType.SERIAL;
    String executorName() default "defaultExecutor";
}
