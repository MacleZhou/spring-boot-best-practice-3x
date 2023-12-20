package cn.javastack.aggregateinmemory.core.support;

import cn.javastack.aggregateinmemory.core.AggregateItemExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class AbstractAggregateItemExecutor<SOURCE_DATA, ITEM_DATA, ITEM_RESULT> implements AggregateItemExecutor<SOURCE_DATA> {
    /**
     * 根据 aggregateKeys 批量获取 AggregateData
     * @param aggregateKeys
     * @return
     */
    protected abstract List<ITEM_DATA> loadAggregateItemsDataByKeys(Map aggregateKeys);

    /**
     * 从 ItemData 中获取 GroupbyKey
     * @param itemData
     * @return
     */
    protected abstract Map<String, Object> createGroupbyKeysFromAggregateItemData(ITEM_DATA itemData);

    /**
     * 将 ItemData 转换为 ItemResult
     * @param itemData
     * @return
     */
    protected abstract ITEM_RESULT convertToResult(ITEM_DATA itemData);

    /**
     * 将 ItemResult 写回至 SourceData
     * @param data
     * @param JoinResults
     */
    protected abstract void onFound(SOURCE_DATA data, List<ITEM_RESULT> JoinResults);

    /**
     * 将 ItemResult分组结果 写回至 SourceData
     * @param data
     * @param groupedByJoinResults
     */
    protected abstract void onFound(SOURCE_DATA data, Map<Map<String, Object>, List<ITEM_RESULT>> groupedByJoinResults);

    /**
     * 未找到对应的 ItemData
     * @param data
     * @param aggregateKeys
     */
    protected abstract void onNotFound(SOURCE_DATA data, Map<String, Object> aggregateKeys);

    @Override
    public void execute(SOURCE_DATA data){
        // 从源数据中提取 所有的AggregateKey
        Map<String, Object> aggregateKeys = null;

        // 根据 JoinKey 获取 JoinData
        List<ITEM_DATA> allItemDatas = loadAggregateItemsDataByKeys(aggregateKeys);

        if(CollectionUtils.isEmpty(allItemDatas)){
            log.info("no item data found");
        }

        List<ITEM_RESULT> itemResults = new ArrayList<>();
        Map<Map<String, Object>, List<ITEM_RESULT>> groupbyMaps = new HashMap<>();
        for(ITEM_DATA itemData : allItemDatas){
            Map<String, Object> groupbyKeys = createGroupbyKeysFromAggregateItemData(itemData);
            if(groupbyKeys != null){

            }
            else {
                log.debug("no group by key defined");
            }
            ITEM_RESULT itemResult = convertToResult(itemData);
            log.debug("success to convert join data {} to join result {}", itemData, itemResult);
            itemResults.add(itemResult);
        }

        if(itemResults.size()>0){
            log.debug("success to convert join data {} to join result {}", aggregateKeys, itemResults);
            onFound(data, itemResults);
        } else {
            log.debug("success to convert join data {} to join result {}", aggregateKeys, itemResults);
            onNotFound(data, aggregateKeys);
        }

        if(CollectionUtils.isEmpty(groupbyMaps)){
            log.debug("no need group by");
        }
        else {
            onFound(data, groupbyMaps);
        }
    }
}
