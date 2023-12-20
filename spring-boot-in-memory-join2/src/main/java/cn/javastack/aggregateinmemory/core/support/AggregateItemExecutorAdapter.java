package cn.javastack.aggregateinmemory.core.support;



import com.google.common.base.Preconditions;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Slf4j
@Builder
@Getter
public class AggregateItemExecutorAdapter<SOURCE_DATA, ITEM_DATA, ITEM_RESULT>
        extends AbstractAggregateItemExecutor<SOURCE_DATA, ITEM_DATA, ITEM_RESULT> {
    private final String name;
    private final int runLevel;

    private final Function<SOURCE_DATA, Map<String, Object>> createKeysFromSourceData;

    private final Function<Map<String, Object>, List<ITEM_DATA>> aggregateDataLoader;

    private final Function<ITEM_DATA, Map<String, Object>> createKeysFromAggregateItemData;

    private final Function<ITEM_DATA, ITEM_RESULT> convertToResult;

    private final BiConsumer<SOURCE_DATA, List<ITEM_RESULT>> foundCallbackList;

    private final BiConsumer<SOURCE_DATA, Map<Map<String, Object>, List<ITEM_RESULT>>> foundCallbackGroupby;

    private final BiConsumer<SOURCE_DATA, Map<String, Object>> lostCallback;

    public AggregateItemExecutorAdapter(String name,
                                        Integer runLevel,
                                        Function<SOURCE_DATA, Map<String, Object>> createKeysFromSourceData,
                                        Function<Map<String, Object>, List<ITEM_DATA>> aggregateDataLoader,
                                        Function<ITEM_DATA, Map<String, Object>> createKeysFromAggregateItemData,
                                        Function<ITEM_DATA, ITEM_RESULT> convertToResult,
                                        BiConsumer<SOURCE_DATA, List<ITEM_RESULT>> foundCallbackList,
                                        BiConsumer<SOURCE_DATA, Map<Map<String, Object>, List<ITEM_RESULT>>> foundCallbackGroupby,
                                        BiConsumer<SOURCE_DATA, Map<String, Object>> lostCallback){
        Preconditions.checkArgument(createKeysFromSourceData != null);
        Preconditions.checkArgument(aggregateDataLoader != null);
        this.name = name;
        this.runLevel = runLevel;
        this.createKeysFromSourceData = createKeysFromSourceData;
        this.aggregateDataLoader = aggregateDataLoader;
        if(createKeysFromAggregateItemData != null) {
            this.createKeysFromAggregateItemData = createKeysFromAggregateItemData;
        } else {
            this.createKeysFromAggregateItemData = defaultCreateGroupbyKeysFromItemData();
        }
        if(foundCallbackList != null) {
            this.foundCallbackList = foundCallbackList;
        }
        else {
            this.foundCallbackList = defaultFoundCallbackList();
        }
        if(foundCallbackGroupby != null) {
            this.foundCallbackGroupby = foundCallbackGroupby;
        } else {
            this.foundCallbackGroupby = defaultFoundCallbackGroupby();
        }
        if(convertToResult != null){
            this.convertToResult = convertToResult;
        } else {
            this.convertToResult = defaultConvertToResult();
        }
        if (lostCallback != null) {
            this.lostCallback = getDefaultLostFunction().andThen(lostCallback);
        }else {
            this.lostCallback = getDefaultLostFunction();
        }
    }

    @Override
    protected List<ITEM_DATA> loadAggregateItemsDataByKeys(Map aggregateKeys) {
        return this.aggregateDataLoader.apply(aggregateKeys);
    }

    @Override
    protected Map<String, Object> createGroupbyKeysFromAggregateItemData(ITEM_DATA itemData) {
        return this.createKeysFromAggregateItemData.apply(itemData);
    }

    @Override
    protected ITEM_RESULT convertToResult(ITEM_DATA itemData) {
        return this.convertToResult.apply(itemData);
    }

    @Override
    protected void onFound(SOURCE_DATA sourceData, List<ITEM_RESULT> joinResults) {
        this.foundCallbackList.accept(sourceData, joinResults);
    }

    @Override
    protected void onFound(SOURCE_DATA sourceData, Map<Map<String, Object>, List<ITEM_RESULT>> groupedByJoinResults) {
        this.foundCallbackGroupby.accept(sourceData, groupedByJoinResults);
    }

    @Override
    protected void onNotFound(SOURCE_DATA sourceData, Map<String, Object> aggregateKeys) {
        this.lostCallback.accept(sourceData, aggregateKeys);
    }

    private BiConsumer<SOURCE_DATA, Map<String, Object>> getDefaultLostFunction() {
        return (data, joinKey) -> {
            log.warn("failed to find join data by {} for {}", joinKey, data);
        };
    }

    private Function<ITEM_DATA, ITEM_RESULT> defaultConvertToResult(){
        return (itemData -> {
           return (ITEM_RESULT) itemData;
        });
    }

    private Function<ITEM_DATA, Map<String, Object>> defaultCreateGroupbyKeysFromItemData(){
        return (itemData -> {
            return null;
        });
    }

    private BiConsumer<SOURCE_DATA, List<ITEM_RESULT>> defaultFoundCallbackList() {
        return (data, itemResults) -> {
            log.warn("ignore foundCallbackList");
        };
    }

    private BiConsumer<SOURCE_DATA, Map<Map<String, Object>, List<ITEM_RESULT>>>  defaultFoundCallbackGroupby() {
        return (data, groupbyItemResultMap) -> {
            log.warn("ignore defaultFoundCallbackGroupby");
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
