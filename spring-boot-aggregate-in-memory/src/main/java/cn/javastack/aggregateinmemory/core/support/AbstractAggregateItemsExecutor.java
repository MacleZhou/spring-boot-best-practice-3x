package cn.javastack.aggregateinmemory.core.support;


import cn.javastack.aggregateinmemory.core.AggregateItemExecutor;
import cn.javastack.aggregateinmemory.core.AggregateItemsExecutor;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by taoli on 2022/7/31.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
abstract class AbstractAggregateItemsExecutor<DATA>
        implements AggregateItemsExecutor<DATA> {
    @Getter(AccessLevel.PROTECTED)
    private final Class<DATA> dataCls;
    @Getter(AccessLevel.PROTECTED)
    private final List<AggregateItemExecutor<DATA>> joinItemExecutors;

    public AbstractAggregateItemsExecutor(Class<DATA> dataCls,
                                          List<AggregateItemExecutor<DATA>> joinItemExecutors) {
        Preconditions.checkArgument(dataCls != null);
        Preconditions.checkArgument(joinItemExecutors != null);

        this.dataCls = dataCls;
        this.joinItemExecutors = joinItemExecutors;
        Collections.sort(this.joinItemExecutors, Comparator.comparingInt(AggregateItemExecutor::runOnLevel));
    }
}
