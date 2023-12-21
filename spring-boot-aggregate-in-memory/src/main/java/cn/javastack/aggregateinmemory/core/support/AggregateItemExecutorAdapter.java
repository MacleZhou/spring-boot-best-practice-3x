package cn.javastack.aggregateinmemory.core.support;

import com.google.common.base.Preconditions;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by taoli on 2022/7/31.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@Slf4j
@Builder
@Getter
public class AggregateItemExecutorAdapter<SOURCE_DATA, JOIN_KEY, JOIN_DATA, RESULT>
        extends AbstractAggregateItemExecutor<SOURCE_DATA, JOIN_KEY, JOIN_DATA, RESULT> {
    private final String name;
    private final int runLevel;

    private final Function<SOURCE_DATA, JOIN_KEY> keyFromSourceData;
    private final Function<JOIN_KEY, List<JOIN_DATA>> joinDataLoader;
    private final Function<JOIN_DATA, JOIN_KEY> keyGroupbyJoinData;
    private final Function<JOIN_DATA, RESULT> joinDataConverter;
    private final BiConsumer<SOURCE_DATA, Object> foundCallback;
    private final BiConsumer<SOURCE_DATA, JOIN_KEY> lostCallback;


    public AggregateItemExecutorAdapter(String name,
                                        Integer runLevel,
                                        Function<SOURCE_DATA, JOIN_KEY> keyFromSourceData,
                                        Function<JOIN_KEY, List<JOIN_DATA>> joinDataLoader,
                                        Function<JOIN_DATA, JOIN_KEY> keyGroupbyJoinData,
                                        Function<JOIN_DATA, RESULT> joinDataConverter,
                                        BiConsumer<SOURCE_DATA, Object> foundCallback,
                                        BiConsumer<SOURCE_DATA, JOIN_KEY> lostCallback) {

        Preconditions.checkArgument(keyFromSourceData != null);
        Preconditions.checkArgument(joinDataLoader != null);
        Preconditions.checkArgument(keyGroupbyJoinData != null);
        Preconditions.checkArgument(joinDataConverter != null);
        Preconditions.checkArgument(foundCallback != null);

        this.name = name;
        this.keyFromSourceData = keyFromSourceData;
        this.joinDataLoader = joinDataLoader;
        this.keyGroupbyJoinData = keyGroupbyJoinData;
        this.joinDataConverter = joinDataConverter;
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
    protected JOIN_KEY createJoinKeyFromSourceData(SOURCE_DATA data) {
        return this.keyFromSourceData.apply(data);
    }

    @Override
    protected Object getJoinDataByJoinKeys(JOIN_KEY joinKey) {
        return this.joinDataLoader.apply(joinKey);
    }

    @Override
    protected JOIN_KEY createJoinKeyFromJoinData(JOIN_DATA joinData) {
        return this.keyGroupbyJoinData.apply(joinData);
    }

    @Override
    protected RESULT convertToResult(JOIN_DATA joinData) {
        return this.joinDataConverter.apply(joinData);
    }

    @Override
    protected void onFound(SOURCE_DATA data, Object joinResult) {
        this.foundCallback.accept(data, joinResult);
    }

    @Override
    protected void onNotFound(SOURCE_DATA data, JOIN_KEY joinKey) {
        this.lostCallback.accept(data, joinKey);
    }

    private BiConsumer<SOURCE_DATA, JOIN_KEY> getDefaultLostFunction() {
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
        return "JoinExecutorAdapter-for-" + name;
    }
}
