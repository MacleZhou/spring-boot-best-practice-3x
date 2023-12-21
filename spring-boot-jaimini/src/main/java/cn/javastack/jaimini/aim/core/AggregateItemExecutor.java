package cn.javastack.jaimini.aim.core;

import java.util.List;

/**
 * 执行数据 aggregate 操作
 */
public interface AggregateItemExecutor<DATA> {
    void execute(List<DATA> datas);

    default int runOnLevel(){
        return 0;
    }
}
