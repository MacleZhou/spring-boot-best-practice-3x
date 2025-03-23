package cn.javastack.jaimini.aim.core.support;

import cn.javastack.jaimini.aim.core.AggregateItemExecutor;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;

import java.util.*;

/**
 * Aggregate一种数据的核心逻辑
 *
 * @param <SOURCE_DATA> 原始数据
 * @param <AGGR_KEY>    join 使用的 key
 * @param <AGGR_DATA>   join 获取的 数据
 * @param <AGGR_RESULT> 转换后的结果数据
 */
@Slf4j
abstract class AbstractAggregateItemExecutor<SOURCE_DATA, AGGR_KEY, AGGR_DATA, AGGR_RESULT> implements AggregateItemExecutor<SOURCE_DATA> {

    /**
     * 从原始数据中生成 AggregateKey
     *
     * @param data
     * @return
     */
    protected abstract AGGR_KEY createAggrKeyFromSourceData(SOURCE_DATA data);

    /**
     * 根据 AggregateKey 批量获取 ItemData, ItemData如果是一个集合，则以List<ItemData>的形式返回
     *
     * @param aggregateKey
     * @return
     */
    protected abstract Object getAggregateItemDataByAggregateKeys(AGGR_KEY aggregateKey);

    /**
     * 从 ItemData 中获取 GroupbyKey用于数据分组
     *
     * @param aggrData
     * @return
     */
    protected abstract AGGR_KEY createGroupbyKeyFromItemData(AGGR_DATA aggrData);

    /**
     * 将 ItemData 转换为 AggregateResult
     *
     * @param aggrData
     * @return
     */
    protected abstract AGGR_RESULT convertToResult(AGGR_DATA aggrData);

    /**
     * 将 AggregateResult 写回至 SourceData, AggregateResult类型必须与SourceData中的对应字段的类型相同
     *
     * @param data
     * @param aggrResult
     */
    protected abstract void onFound(SOURCE_DATA data, Object aggrResult);

    /**
     * 未找到对应的 AggregateData
     *
     * @param data
     * @param aggrKey
     */
    protected abstract void onNotFound(SOURCE_DATA data, AGGR_KEY aggrKey);

    @Override
    public void execute(List<SOURCE_DATA> sourceDatas) {
        // 从源数据中提取 JoinKey
        AGGR_KEY aggregateKey = this.createAggrKeyFromSourceData(sourceDatas.get(0));
        if(null == aggregateKey){
            log.warn("aggregateKey is null");
            return;
        }
        log.debug("aggregateKey:" + JSON.toJSONString(aggregateKey));

        // 根据 AggregateKey 从数据库中获取 AggregateData
        Object resultDataFromLoader = getAggregateItemDataByAggregateKeys(aggregateKey);
        if (null == resultDataFromLoader) {
            log.warn("no aggregate item data found");
            onNotFound(sourceDatas.get(0), aggregateKey);
            return;
        }

        /**
         * There are some ORM framework return an Optional<?> object, thus need to handle and check from here
         * */
        if(resultDataFromLoader instanceof Optional<?>){
            if(!((Optional<?>)resultDataFromLoader).isPresent()){
                log.warn("no aggregate item data found");
                onNotFound(sourceDatas.get(0), aggregateKey);
                return;
            }
            resultDataFromLoader = ((Optional<?>)resultDataFromLoader).get();
        }

        log.debug("get join data {} by join key {}", resultDataFromLoader, aggregateKey);
        if (List.class.isAssignableFrom(resultDataFromLoader.getClass())) {
            List<AGGR_DATA> allOriginalItemDatas = (List<AGGR_DATA>) resultDataFromLoader;
            List<AGGR_RESULT> covertedAggregateItems = new ArrayList<>();
            // 将 AggregateData 以 Map 形式进行组织
            Map<AGGR_KEY, List<AGGR_RESULT>> convertedGroupbyAggregatedItemsMap = new HashMap<>();
            for (AGGR_DATA beforeItemData : allOriginalItemDatas) {
                AGGR_RESULT convertedItemResult = convertToResult(beforeItemData);
                covertedAggregateItems.add(convertedItemResult);
                AGGR_KEY groupbyKey = this.createGroupbyKeyFromItemData(beforeItemData);
                if(groupbyKey != null){
                    List<AGGR_RESULT> listJoinData = convertedGroupbyAggregatedItemsMap.getOrDefault(groupbyKey, Lists.newArrayList());
                    listJoinData.add(convertedItemResult);
                    convertedGroupbyAggregatedItemsMap.put(groupbyKey, listJoinData);
                }
            }
            if(convertedGroupbyAggregatedItemsMap.size()>0){
                onFound(sourceDatas.get(0), convertedGroupbyAggregatedItemsMap);
            }
            else {
                onFound(sourceDatas.get(0), covertedAggregateItems);
            }
        } else {
            AGGR_RESULT itemResult = convertToResult((AGGR_DATA) resultDataFromLoader);
            onFound(sourceDatas.get(0), itemResult);
        }
    }
}
