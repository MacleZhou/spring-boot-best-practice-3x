package cn.javastack.annotation.wide;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by taoli on 2022/10/26.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BindFrom {

    /**
     * 来源
     * @return
     */
    Class sourceClass();

    /**
     * 字段名称
     * @return
     */
    String field();
}
