package cn.javastack.aggregateinmemory.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AggregateInMemory {
    /**
     * 从 sourceData 中提取 key
     * @return
     */
    String[] keysFromSourceData();


    /**
     * 批量数据抓取
     * @return
     */
    String loader();

    /**
     * 从 aggregateData 中提取 key
     * @return
     */
    String[] keysFromAggregateData() default {};

    /**
     * 结果转换器
     * @return
     */
    String dataConverter() default "";

    /**
     * 运行级别，同一级别的 join 可 并行执行
     * @return
     */
    int runLevel() default 10;
}
