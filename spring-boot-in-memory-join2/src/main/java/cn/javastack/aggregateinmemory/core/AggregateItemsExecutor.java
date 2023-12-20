package cn.javastack.aggregateinmemory.core;

import java.util.List;

public interface AggregateItemsExecutor<DATA> {
    void execute(List<DATA> data);

    default int runOnLevel(){
        return 0;
    }
}
