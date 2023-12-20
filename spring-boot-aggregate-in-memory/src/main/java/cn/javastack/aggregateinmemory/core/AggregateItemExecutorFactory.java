package cn.javastack.aggregateinmemory.core;

import java.util.List;

/**
 * Created by taoli on 2022/8/5.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
public interface AggregateItemExecutorFactory {
    <DATA> List<AggregateItemExecutor<DATA>> createForType(Class<DATA> cls);
}
