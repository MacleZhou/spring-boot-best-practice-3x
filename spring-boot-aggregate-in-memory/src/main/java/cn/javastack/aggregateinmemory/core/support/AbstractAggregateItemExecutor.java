package cn.javastack.aggregateinmemory.core.support;

import cn.javastack.aggregateinmemory.core.AggregateItemExecutor;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by taoli on 2022/7/31.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 *
 * @param <SOURCE_DATA> 原始数据
 * @param <JOIN_KEY>    join 使用的 key
 * @param <JOIN_DATA>   join 获取的 数据
 * @param <JOIN_RESULT> 转换后的结果数据
 */
@Slf4j
abstract class AbstractAggregateItemExecutor<SOURCE_DATA, JOIN_KEY, JOIN_DATA, JOIN_RESULT> implements AggregateItemExecutor<SOURCE_DATA> {

    /**
     * 从原始数据中生成 JoinKey
     *
     * @param data
     * @return
     */
    protected abstract JOIN_KEY createJoinKeyFromSourceData(SOURCE_DATA data);

    /**
     * 根据 JoinKey 批量获取 JoinData
     *
     * @param joinKey
     * @return
     */
    protected abstract Object getJoinDataByJoinKeys(JOIN_KEY joinKey);

    /**
     * 从 JoinData 中获取 JoinKey
     *
     * @param joinData
     * @return
     */
    protected abstract JOIN_KEY createJoinKeyFromJoinData(JOIN_DATA joinData);

    /**
     * 将 JoinData 转换为 JoinResult
     *
     * @param joinData
     * @return
     */
    protected abstract JOIN_RESULT convertToResult(JOIN_DATA joinData);

    /**
     * 将 JoinResult 写回至 SourceData
     *
     * @param data
     * @param joinResult
     */
    protected abstract void onFound(SOURCE_DATA data, Object joinResult);

    /**
     * 未找到对应的 JoinData
     *
     * @param data
     * @param joinKey
     */
    protected abstract void onNotFound(SOURCE_DATA data, JOIN_KEY joinKey);

    @Override
    public void execute(List<SOURCE_DATA> sourceDatas) {
        // 从源数据中提取 JoinKey
        /*
        List<JOIN_KEY> joinKeys = sourceDatas.stream()
                .filter(Objects::nonNull)
                .map(this::createJoinKeyFromSourceData)
                .filter(Objects::nonNull)
                .distinct()
                .collect(toList());
        log.debug("get join key {} from source data {}", joinKeys, sourceDatas);
        */

        JOIN_KEY joinKey = this.createJoinKeyFromSourceData(sourceDatas.get(0));

        log.info("joinKey:" + JSON.toJSONString(joinKey));


        // 根据 AggregateKey 从数据库中获取 AggregateData
        Object resultDataFromLoader = getJoinDataByJoinKeys(joinKey);
        if (null == resultDataFromLoader) {
            log.info("no item data found");
            onNotFound(sourceDatas.get(0), joinKey);
            return;
        }

        if (List.class.isAssignableFrom(resultDataFromLoader.getClass())) {
            List<JOIN_DATA> allJoinDatas = (List<JOIN_DATA>) resultDataFromLoader;
            log.debug("get join data {} by join key {}", allJoinDatas, joinKey);

            List<JOIN_RESULT> itemResults = new ArrayList<>();
            // 将 JoinData 以 Map 形式进行组织
            Map<JOIN_KEY, List<JOIN_RESULT>> joinDataMap = new HashMap<>();

            for (JOIN_DATA joinData : allJoinDatas) {
                JOIN_RESULT itemResult = convertToResult(joinData);
                itemResults.add(itemResult);
                JOIN_KEY groupbyKey = this.createJoinKeyFromJoinData(joinData);
                if(groupbyKey != null){
                    List<JOIN_RESULT> listJoinData = joinDataMap.getOrDefault(groupbyKey, Lists.newArrayList());
                    listJoinData.add(itemResult);
                    joinDataMap.put(groupbyKey, listJoinData);
                }
            }
            if(joinDataMap.size()>0){
                onFound(sourceDatas.get(0), joinDataMap);
            }
            else {
                onFound(sourceDatas.get(0), itemResults);
            }
        } else {
            JOIN_RESULT itemResult = convertToResult((JOIN_DATA) resultDataFromLoader);
            onFound(sourceDatas.get(0), itemResult);
        }


        /*


        // 处理每一条 SourceData
        for (SOURCE_DATA data : sourceDatas){
            // 从 SourceData 中 获取 JoinKey
            JOIN_KEY joinKey = createJoinKeyFromSourceData(data);
            if (joinKey == null){
                log.warn("join key from join data {} is null", data);
                continue;
            }
            // 根据 JoinKey 获取 JoinData
            List<JOIN_DATA> joinDatasByKey = joinDataMap.get(joinKey);
            if (CollectionUtils.isNotEmpty(joinDatasByKey)){
                // 获取到 JoinData， 转换为 JoinResult，进行数据写回
                List<JOIN_RESULT> joinResults = joinDatasByKey.stream()
                        .filter(Objects::nonNull)
                        .map(joinData -> convertToResult(joinData))
                        .collect(toList());

                log.debug("success to convert join data {} to join result {}", joinDatasByKey, joinResults);
                onFound(data, joinResults);
                log.debug("success to write join result {} to source data {}", joinResults, data);
            }else {
                log.warn("join data lost by join key {} for source data {}", joinKey, data);
                // 为获取到 JoinData，进行 notFound 回调
                onNotFound(data, joinKey);
            }
        }*/
    }
}
