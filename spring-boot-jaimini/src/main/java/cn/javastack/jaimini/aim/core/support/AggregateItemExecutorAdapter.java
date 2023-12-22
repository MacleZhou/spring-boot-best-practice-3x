package cn.javastack.jaimini.aim.core.support;

import com.google.common.base.Preconditions;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 定义AggregateItemExecutor的Adaptor
 */
@Slf4j
@Builder
@Getter
public class AggregateItemExecutorAdapter<SOURCE_DATA, AGGR_KEY, AGGR_DATA, RESULT>
        extends AbstractAggregateItemExecutor<SOURCE_DATA, AGGR_KEY, AGGR_DATA, RESULT> {
    private final String name;
    private final int runLevel;

    private final Function<SOURCE_DATA, AGGR_KEY> keyFromSourceData;
    private final Function<AGGR_KEY, List<AGGR_DATA>> aggregateDataLoader;
    private final Function<AGGR_DATA, AGGR_KEY> keyGroupbyItemData;
    private final Function<AGGR_DATA, RESULT> dataConverter;
    private final BiConsumer<SOURCE_DATA, Object> foundCallback;
    private final BiConsumer<SOURCE_DATA, AGGR_KEY> lostCallback;


    public AggregateItemExecutorAdapter(String name,
                                        Integer runLevel,
                                        Function<SOURCE_DATA, AGGR_KEY> keyFromSourceData,
                                        Function<AGGR_KEY, List<AGGR_DATA>> aggregateDataLoader,
                                        Function<AGGR_DATA, AGGR_KEY> keyGroupbyItemData,
                                        Function<AGGR_DATA, RESULT> dataConverter,
                                        BiConsumer<SOURCE_DATA, Object> foundCallback,
                                        BiConsumer<SOURCE_DATA, AGGR_KEY> lostCallback) {

        Preconditions.checkArgument(keyFromSourceData != null);
        Preconditions.checkArgument(aggregateDataLoader != null);
        Preconditions.checkArgument(keyGroupbyItemData != null);
        Preconditions.checkArgument(dataConverter != null);
        Preconditions.checkArgument(foundCallback != null);

        this.name = name;
        this.keyFromSourceData = keyFromSourceData;
        this.aggregateDataLoader = aggregateDataLoader;
        this.keyGroupbyItemData = keyGroupbyItemData;
        this.dataConverter = dataConverter;
        this.foundCallback = foundCallback;

        if (lostCallback != null) {
            this.lostCallback = getDefaultLostFunction().andThen(lostCallback);
        }else {
            this.lostCallback = getDefaultLostFunction();
        }

        if (runLevel == null){
            this.runLevel = 0;
        }else {
            this.runLevel = runLevel.intValue();
        }
    }

    @Override
    protected AGGR_KEY createAggrKeyFromSourceData(SOURCE_DATA data) {
        return this.keyFromSourceData.apply(data);
    }

    @Override
    protected Object getAggregateItemDataByAggregateKeys(AGGR_KEY aggregateKey) {
        return this.aggregateDataLoader.apply(aggregateKey);
    }

    @Override
    protected AGGR_KEY createGroupbyKeyFromItemData(AGGR_DATA aggrData) {
        return this.keyGroupbyItemData.apply(aggrData);
    }

    @Override
    protected RESULT convertToResult(AGGR_DATA aggrData) {
        return this.dataConverter.apply(aggrData);
    }

    @Override
    protected void onFound(SOURCE_DATA data, Object aggrResult) {
        this.foundCallback.accept(data, aggrResult);
    }

    @Override
    protected void onNotFound(SOURCE_DATA data, AGGR_KEY joinKey) {
        this.lostCallback.accept(data, joinKey);
    }

    private BiConsumer<SOURCE_DATA, AGGR_KEY> getDefaultLostFunction() {
        return (data, joinKey) -> {
            log.warn("failed to find join data by {} for {}", joinKey, data);
        };
    }

    @Override
    public int runOnLevel() {
        return this.runLevel;
    }

    @Override
    public String toString() {
        return "AggregateExecutorAdapter-for-" + name;
    }
}
