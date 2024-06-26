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
        loader = "#{@addressRepository.getByIds(#root)}",
        dataConverter = "#{T(cn.javastack.data.loader.join.jimdemo.vo.AddressVO).apply(#root)}"
)
public @interface JoinAddressVOOnId {
    @AliasFor(
            annotation = DataLoaderInMemory.class
    )
    String keyFromSourceData();
}
