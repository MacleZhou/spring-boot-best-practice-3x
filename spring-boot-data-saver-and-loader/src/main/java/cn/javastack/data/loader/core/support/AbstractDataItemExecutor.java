package cn.javastack.data.loader.core.support;

import cn.javastack.data.loader.annotation.DataHolderType;
import cn.javastack.data.loader.core.DataItemExecutor;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * 执行器，实现一个Field的join
 *
 * @param <SOURCE_DATA> 原始数据
 * @param <JOIN_KEY> join 使用的 key
 * @param <JOIN_DATA> join 获取的 数据
 * @param <JOIN_RESULT> 转换后的结果数据
 */
@Slf4j
abstract class AbstractDataItemExecutor<SOURCE_DATA, JOIN_KEY, JOIN_DATA, JOIN_RESULT> implements DataItemExecutor<SOURCE_DATA> {

    /**
     * 从原始数据中生成 JoinKey
     * @param data
     * @return
     */
    protected abstract JOIN_KEY createJoinKeyFromSourceData(SOURCE_DATA data);

    /**
     * 根据 JoinKey 批量获取 JoinData
     * @param joinKey
     * @return
     */
    protected abstract Object getJoinDataByJoinKey(Object joinKey);

    /**
     * 从 JoinData 中获取 JoinKey
     * @param joinData
     * @return
     */
    protected abstract JOIN_KEY createJoinKeyFromJoinData(JOIN_DATA joinData);

    /**
     * 将 JoinData 转换为 JoinResult
     * @param joinData
     * @return
     */
    protected abstract JOIN_RESULT convertToResult(JOIN_DATA joinData);

    /**
     * 将 JoinResult 写回至 SourceData
     * @param data
     * @param joinResults //It can be a List, or a Map, or alone object, like Product
     */
    protected abstract void onFound(SOURCE_DATA data, Object joinResults);

    /**
     * 未找到对应的 JoinData
     * @param data
     * @param joinKey
     */
    protected abstract void onNotFound(SOURCE_DATA data, JOIN_KEY joinKey);

    @Override
    public void execute(List<SOURCE_DATA> sourceDatas, DataHolderType dataHolderType) {
        if(dataHolderType.equals(DataHolderType.JOIN)) {
            join(sourceDatas);
        }
        else {
            // 从源数据中提取 JoinKey
            JOIN_KEY aggregateKey = this.createJoinKeyFromSourceData(sourceDatas.get(0));
            if(null == aggregateKey){
                log.warn("aggregateKey is null");
                return;
            }
            log.debug("aggregateKey:" + JSON.toJSONString(aggregateKey));

            // 根据 AggregateKey 从数据库中获取 AggregateData
            Object resultDataFromLoader = this.getJoinDataByJoinKey(aggregateKey);
            if (null == resultDataFromLoader) {
                log.warn("no aggregate item data found");
                onNotFound(sourceDatas.get(0), aggregateKey);
                return;
            }

            log.debug("get join data {} by join key {}", resultDataFromLoader, aggregateKey);
            if (List.class.isAssignableFrom(resultDataFromLoader.getClass())) {
                List<JOIN_DATA> allOriginalItemDatas = (List<JOIN_DATA>) resultDataFromLoader;
                List<JOIN_RESULT> covertedAggregateItems = new ArrayList<>();
                // 将 AggregateData 以 Map 形式进行组织
                Map<JOIN_KEY, List<JOIN_RESULT>> convertedGroupbyAggregatedItemsMap = new HashMap<>();
                for (JOIN_DATA beforeItemData : allOriginalItemDatas) {
                    JOIN_RESULT convertedItemResult = convertToResult(beforeItemData);
                    covertedAggregateItems.add(convertedItemResult);
                    JOIN_KEY groupbyKey = this.createJoinKeyFromJoinData(beforeItemData);
                    if(groupbyKey != null){
                        List<JOIN_RESULT> listJoinData = convertedGroupbyAggregatedItemsMap.getOrDefault(groupbyKey, Lists.newArrayList());
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
                JOIN_RESULT itemResult = convertToResult((JOIN_DATA) resultDataFromLoader);
                onFound(sourceDatas.get(0), itemResult);
            }
        }
    }

    private void join(List<SOURCE_DATA> sourceDatas) {
        // 从源数据中提取 JoinKey
        List<JOIN_KEY> joinKeys = sourceDatas.stream()
                .filter(Objects::nonNull)
                .map(this::createJoinKeyFromSourceData)
                .filter(Objects::nonNull)
                .distinct()
                .collect(toList());
        log.debug("get join key {} from source data {}", joinKeys, sourceDatas);

        // 根据 JoinKey 获取 JoinData
        Object loadedDataResult = getJoinDataByJoinKey(joinKeys);
        List<JOIN_DATA> allJoinDatas = null == loadedDataResult ? new ArrayList<>() : (List<JOIN_DATA>) loadedDataResult;
        log.debug("get join data {} by join key {}", allJoinDatas, joinKeys);

        // 将 JoinData 以 Map 形式进行组织
        Map<JOIN_KEY, List<JOIN_DATA>> joinDataMap = allJoinDatas.stream()
                .filter(Objects::nonNull)
                .collect(groupingBy(this::createJoinKeyFromJoinData));
        log.debug("group by join key, result is {}", joinDataMap);

        // 处理每一条 SourceData
        for (SOURCE_DATA data : sourceDatas) {
            // 从 SourceData 中 获取 JoinKey
            JOIN_KEY joinKey = createJoinKeyFromSourceData(data);
            if (joinKey == null) {
                log.warn("join key from join data {} is null", data);
                continue;
            }
            // 根据 JoinKey 获取 JoinData
            List<JOIN_DATA> joinDatasByKey = joinDataMap.get(joinKey);
            if (CollectionUtils.isNotEmpty(joinDatasByKey)) {
                // 获取到 JoinData， 转换为 JoinResult，进行数据写回
                List<JOIN_RESULT> joinResults = joinDatasByKey.stream()
                        .filter(Objects::nonNull)
                        .map(joinData -> convertToResult(joinData))
                        .collect(toList());

                log.debug("success to convert join data {} to join result {}", joinDatasByKey, joinResults);
                onFound(data, joinResults);
                log.debug("success to write join result {} to source data {}", joinResults, data);
            } else {
                log.warn("join data lost by join key {} for source data {}", joinKey, data);
                // 为获取到 JoinData，进行 notFound 回调
                onNotFound(data, joinKey);
            }
        }
    }
}
