package cn.javastack.data.loader.join.jimdemo.annotation;

import cn.javastack.data.loader.annotation.DataLoaderInMemory;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by taoli on 2022/7/31.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@DataLoaderInMemory(keyFromSourceData = "",
        keyFromJoinData = "#{id}",
        loader = "#{@productRepository.getByIds(#root)}",
        dataConverter = "#{T(cn.javastack.data.loader.join.jimdemo.vo.ProductVO).apply(#root)}"
)
public @interface JoinProductVOOnId {
    @AliasFor(
            annotation = DataLoaderInMemory.class
    )
    String keyFromSourceData();
}
