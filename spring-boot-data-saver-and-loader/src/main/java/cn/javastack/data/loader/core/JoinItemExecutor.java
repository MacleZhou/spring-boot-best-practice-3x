package cn.javastack.data.loader.core;

import cn.javastack.data.loader.annotation.DataHolderType;

import java.util.List;

/**
 * 执行数据 join 操作
 */
public interface JoinItemExecutor<DATA> {
    void execute(List<DATA> datas, DataHolderType dataHolderType);

    default int runOnLevel(){
        return 0;
    }
}
