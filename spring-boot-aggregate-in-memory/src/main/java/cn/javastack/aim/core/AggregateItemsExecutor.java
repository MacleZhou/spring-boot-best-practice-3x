package cn.javastack.aim.core;

import java.util.List;

/**
 * Aggregate executor
 */
public interface AggregateItemsExecutor<DATA> {
    void execute(List<DATA> datas);
}
