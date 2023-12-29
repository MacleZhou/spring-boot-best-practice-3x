package cn.javastack.data.loader.core.support;


import cn.javastack.data.loader.annotation.DataHolderType;
import cn.javastack.data.loader.core.DataItemExecutor;
import cn.javastack.data.loader.core.DataItemsExecutor;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 根据runLevel由小到大排序所有的执行器
 */
abstract class AbstractDataItemsExecutor<DATA>
        implements DataItemsExecutor<DATA> {

    @Getter(AccessLevel.PROTECTED)
    private final Class<DATA> dataCls;

    @Getter(AccessLevel.PROTECTED)
    private DataHolderType dataHolderType;


    @Getter(AccessLevel.PROTECTED)
    private final List<DataItemExecutor<DATA>> dataItemExecutors;

    public AbstractDataItemsExecutor(Class<DATA> dataCls,
                                     DataHolderType dataHolderType,
                                     List<DataItemExecutor<DATA>> dataItemExecutors) {
        Preconditions.checkArgument(dataCls != null);
        this.dataHolderType = (null == dataHolderType) ? DataHolderType.AGGREGATE : dataHolderType;
        Preconditions.checkArgument(dataItemExecutors != null);

        this.dataCls = dataCls;
        this.dataItemExecutors = dataItemExecutors;
        Collections.sort(this.dataItemExecutors, Comparator.comparingInt(DataItemExecutor::runOnLevel));
    }
}
