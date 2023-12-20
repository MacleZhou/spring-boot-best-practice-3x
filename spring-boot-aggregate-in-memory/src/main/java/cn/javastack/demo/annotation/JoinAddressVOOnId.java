package cn.javastack.demo.annotation;

import cn.javastack.aggregateinmemory.annotation.AggregateInMemory;
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
@AggregateInMemory(keyFromSourceData = "",
        keyFromJoinData = "#{id}",
        loader = "#{@addressRepository.getByIds(#root)}",
        joinDataConverter = "#{T(cn.javastack.joininmemory.demo.AddressVO).apply(#root)}"
)
public @interface JoinAddressVOOnId {
    @AliasFor(
            annotation = AggregateInMemory.class
    )
    String keyFromSourceData();
}
