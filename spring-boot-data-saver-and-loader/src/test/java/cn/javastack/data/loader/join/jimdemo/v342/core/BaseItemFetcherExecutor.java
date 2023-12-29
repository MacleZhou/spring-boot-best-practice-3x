package cn.javastack.data.loader.join.jimdemo.v342.core;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * 采用模版的设计范式进行抽取, 比如从一组关联对象(OrderVO)中取出关联值List(addressId), 到Address表中根据addressId查询出所有的Address记录，
 * 把每一个DATA(Address)转换成RESULT(AddressVO)对象，在返回(setResult)
 *
 * FETCHER - 需要抽取的数据对象类型, 比如AddressVOFetcher
 * DATA - 数据库表中返回的Entity类型, 比如Address
 * RESULT - 由DATA转换后的值对象类型，比如AddressVO
 * **/
public abstract class BaseItemFetcherExecutor<FETCHER extends ItemFetcher, DATA, RESULT>
        implements ItemFetcherExecutor<FETCHER> {

    @Override
    public void fetch(List<FETCHER> fetchers) {
        // 获取关联信息
        List<Long> ids = fetchers.stream()
                .map(this::getFetchId)
                .distinct()
                .collect(Collectors.toList());
        // 查询关联数据
        List<DATA> datas = loadData(ids);
        // 转为为 Map
        Map<Long, List<DATA>> dataMap = datas.stream()
                .collect(groupingBy(this::getDataId));
        // 依次进行数据绑定
        fetchers.forEach(fetcher -> {
            Long id = getFetchId(fetcher);
            List<DATA> ds = dataMap.get(id);
            if (ds != null){
                // 转换为 VO
                List<RESULT> result = ds.stream()
                        .map( data -> convertToVo(data))
                        .collect(Collectors.toList());
                // 将数据写回到结果
                setResult(fetcher, result);
            }
        });
    }

    protected abstract Long getFetchId(FETCHER fetcher);

    protected abstract List<DATA> loadData(List<Long> ids);

    protected abstract Long getDataId(DATA data);

    protected abstract RESULT convertToVo(DATA data);

    protected abstract void setResult(FETCHER fetcher, List<RESULT> result);
}