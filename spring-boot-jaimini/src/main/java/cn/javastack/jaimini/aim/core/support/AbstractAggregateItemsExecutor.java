package cn.javastack.jaimini.aim.core.support;


import cn.javastack.jaimini.aim.core.AggregateItemExecutor;
import cn.javastack.jaimini.aim.core.AggregateItemsExecutor;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 排序所有的执行器
 */
abstract class AbstractAggregateItemsExecutor<DATA>
        implements AggregateItemsExecutor<DATA> {
    @Getter(AccessLevel.PROTECTED)
    private final Class<DATA> dataCls;
    @Getter(AccessLevel.PROTECTED)
    private final List<AggregateItemExecutor<DATA>> aggregateItemExecutors;

    public AbstractAggregateItemsExecutor(Class<DATA> dataCls,
                                          List<AggregateItemExecutor<DATA>> aggregateItemExecutors) {
        Preconditions.checkArgument(dataCls != null);
        Preconditions.checkArgument(aggregateItemExecutors != null);

        this.dataCls = dataCls;
        this.aggregateItemExecutors = aggregateItemExecutors;
        Collections.sort(this.aggregateItemExecutors, Comparator.comparingInt(AggregateItemExecutor::runOnLevel));
    }
}
