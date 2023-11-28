package cn.javastack.service.stock;


import cn.javastack.annotation.loader.LazyLoadBy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@LazyLoadBy("#{@stockRepository.getByProductId(${productId})}")
public @interface LazyLoadStockByProductId {
    String productId();
}
