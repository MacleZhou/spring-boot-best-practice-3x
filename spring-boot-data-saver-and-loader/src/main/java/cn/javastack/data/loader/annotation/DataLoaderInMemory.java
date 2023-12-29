package cn.javastack.data.loader.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义内存加载的属性，内存加载分两种情况如下：
 * 1. Join - 比如order.userId join到User表
 * 2. Aggregate - 比如一个order.products 一个order中有0到多个product
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataLoaderInMemory {
    /**
     * Spring Expression从 sourceData 中提取 key, 比如#{order.userId},及从order中的userId值去join User<br>
     * 表中的User对象
     * @return
     */
    String keyFromSourceData();

    /**
     * Spring Expression 用于批量数据抓取, 比如“#{@userRepository.getByIds(#root)}”，及调用userRepository获取<br>
     * 全部的User对象根据给定的用户ID集合，这些ID集合是通过keyFromSourceData()从所有的Order中取到的
     * @return
     */
    String loader();


    /**
     * loadedData（对于loader回来的数据） 中提取 key，如何要用于Join(比如把user join到order对象中)或返回一个Map分组，<br>
     * 比如Map<String, Product>
     * 如果不用Join或分组，直接返回Product 或 List<Product>，则此值不能设置，否则必须设置
     * @return
     */
    String keyFromJoinData() default "";


    /**
     * 结果转换器, 比如返回的User需要转成UserVO，则需要提供函数，否则不用提供
     * @return
     */
    String dataConverter() default "";

    /**
     * 运行级别，同一级别的 join 可 并行执行，对于不同级别的，从小到大执行
     * @return
     */
    int runLevel() default 10;
}
