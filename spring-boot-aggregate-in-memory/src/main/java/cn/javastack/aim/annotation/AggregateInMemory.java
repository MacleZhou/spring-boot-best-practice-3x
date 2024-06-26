package cn.javastack.aim.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by taoli on 2022/8/5.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AggregateInMemory {
    /**
     * 从 sourceData 中提取 key
     * @return
     */
    String keyFromSourceData();

    /**
     * 从 joinData 中提取 key
     * @return
     */
    String keyToGroupbyItemData() default "";

    /**
     * 批量数据抓取
     * @return
     */
    String loader();

    /**
     * 结果转换器
     * @return
     */
    String itemDataConverter() default "";

    /**
     * 运行级别，同一级别的 join 可 并行执行
     * @return
     */
    int runLevel() default 10;
}
