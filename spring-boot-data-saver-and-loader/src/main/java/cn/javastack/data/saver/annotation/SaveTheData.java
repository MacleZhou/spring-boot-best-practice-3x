package cn.javastack.data.saver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义每个PO的字段级别的数据保存相关
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SaveTheData {
    UpdatePolicy updatePolicy() default UpdatePolicy.DeleteAllAndInsert;

    /**
     * Converter 负责把PO转成Entity，如果没有定义，这不需要转换，及AggregatedPO中持有的就是Entity对象了
     * */
    String entityConverter() default "";

    /**
     * 主键指定
     * */
    String primaryKey();

    /**
     * ParentKey, 如果一个表是子表，用此处定义如何获取到父表的主键，用于填入到子表对应的子段上
     * */
    String parentKey() default "";

    /**
     * 当一个表有父表时，在插入前需要把parentKey返回的值放入到对应的parentKey字段中
     * */
    String setParentKey() default "";

    /**
     * 比较俩条记录是否相等，如果不指定，用主键比较
     * */
    String equalsCompare() default "";
}
