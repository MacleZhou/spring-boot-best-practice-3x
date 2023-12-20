package cn.javastack.aggregateinmemory.core;

public interface AggregateItemExecutor<SOURCE_DATA> {
    void execute(SOURCE_DATA data);

    default int runOnLevel(){
        return 0;
    }
}
