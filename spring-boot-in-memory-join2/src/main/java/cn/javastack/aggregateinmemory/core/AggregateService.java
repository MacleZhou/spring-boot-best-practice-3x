package cn.javastack.aggregateinmemory.core;


import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

public interface AggregateService {
    /**
     * 执行内存 join
     * @param t
     */
    default <T> void aggregateInMemory(T t){
        if (t == null){
            return;
        }
        aggregateInMemory((Class<T>) t.getClass(), Collections.singletonList(t));
    }

    default <T> void aggregateInMemory(List<T> t){
        if (CollectionUtils.isEmpty(t)){
            return;
        }
        if (t.size() == 1){
            aggregateInMemory(t.get(0));
        }else {
            aggregateInMemory((Class<T>) t.get(0).getClass(), t);
        }
    }

    /**
     * 执行内存 aggregate
     * @param t 需要聚合的对象
     */
    <T> void aggregateInMemory(Class<T> tCls, List<T> t);

    /**
     * 注册一个类型，主要用于初始化
     * @param tCls
     * @param <T>
     */
    <T> void register(Class<T> tCls);
}
