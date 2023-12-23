package cn.javastack.constants;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConstantKV {
    String value() default "";

    boolean cacheable() default false;

    String k() default "";

    String v() default "";
}
