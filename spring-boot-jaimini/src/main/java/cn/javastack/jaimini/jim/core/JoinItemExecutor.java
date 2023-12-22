package cn.javastack.jaimini.jim.core;

import java.util.List;

/**
 * 执行数据 join 操作
 */
public interface JoinItemExecutor<DATA> {
    void execute(List<DATA> datas);

    default int runOnLevel(){
        return 0;
    }
}
