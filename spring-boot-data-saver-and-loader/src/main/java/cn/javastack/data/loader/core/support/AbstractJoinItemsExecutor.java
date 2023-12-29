package cn.javastack.data.loader.core.support;


import cn.javastack.data.loader.annotation.DataHolderType;
import cn.javastack.data.loader.core.JoinItemExecutor;
import cn.javastack.data.loader.core.JoinItemsExecutor;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 根据runLevel由小到大排序所有的执行器
 */
abstract class AbstractJoinItemsExecutor<DATA>
        implements JoinItemsExecutor<DATA> {

    @Getter(AccessLevel.PROTECTED)
    private final Class<DATA> dataCls;

    @Getter(AccessLevel.PROTECTED)
    private DataHolderType dataHolderType;


    @Getter(AccessLevel.PROTECTED)
    private final List<JoinItemExecutor<DATA>> joinItemExecutors;

    public AbstractJoinItemsExecutor(Class<DATA> dataCls,
                                     DataHolderType dataHolderType,
                                     List<JoinItemExecutor<DATA>> joinItemExecutors) {
        Preconditions.checkArgument(dataCls != null);
        this.dataHolderType = (null == dataHolderType) ? DataHolderType.AGGREGATE : dataHolderType;
        Preconditions.checkArgument(joinItemExecutors != null);

        this.dataCls = dataCls;
        this.joinItemExecutors = joinItemExecutors;
        Collections.sort(this.joinItemExecutors, Comparator.comparingInt(JoinItemExecutor::runOnLevel));
    }
}
