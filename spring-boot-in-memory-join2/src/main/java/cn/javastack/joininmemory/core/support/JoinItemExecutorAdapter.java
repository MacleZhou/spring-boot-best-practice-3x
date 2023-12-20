package cn.javastack.joininmemory.core.support;

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
public class JoinItemExecutorAdapter<SOURCE_DATA, JOIN_KEY, JOIN_DATA, RESULT>
        extends AbstractJoinItemExecutor<SOURCE_DATA, JOIN_KEY, JOIN_DATA, RESULT> {
    private final String name;
    private final int runLevel;

    //调用此函数接口从SOURCE_DATA, 获取JOIN_KEY
    private final Function<SOURCE_DATA, JOIN_KEY> keyFromSourceData;

    //调用此函数接口根据一组List<JOIN_KEY>, 从数据库查询并返回所有的数据记录LIST<JOIN_DATA>
    private final Function<List<JOIN_KEY>, List<JOIN_DATA>> joinDataLoader;

    //调用此函数接口从查询结果就是数据库中返回的数据记录JOIN_DATA(单行记录), 返回JOIN_KEY的值
    private final Function<JOIN_DATA, JOIN_KEY> keyFromJoinData;

    //调用此函数接口转换查询结果，JOIN_DATA就是数据库中返回的数据记录(单行记录), 返回结果为RESULT对象
    private final Function<JOIN_DATA, RESULT> joinDataConverter;

    //如果查找到数据，则调用此函数接口，把结果List<RESULT>写入到SOURCE_DATA中
    private final BiConsumer<SOURCE_DATA, List<RESULT>> foundCallback;

    //如果没有查找到数据，则调用此函数接口，把结果对应的JOIN_KEY写入到SOURCE_DATA中
    private final BiConsumer<SOURCE_DATA, JOIN_KEY> lostCallback;


    public JoinItemExecutorAdapter(String name,
                                   Integer runLevel,
                                   Function<SOURCE_DATA, JOIN_KEY> keyFromSourceData,
                                   Function<List<JOIN_KEY>, List<JOIN_DATA>> joinDataLoader,
                                   Function<JOIN_DATA, JOIN_KEY> keyFromJoinData,
                                   Function<JOIN_DATA, RESULT> joinDataConverter,
                                   BiConsumer<SOURCE_DATA, List<RESULT>> foundCallback,
                                   BiConsumer<SOURCE_DATA, JOIN_KEY> lostCallback) {
        log.debug("jim.JoinItemExecutorAdapter.new");
        Preconditions.checkArgument(keyFromSourceData != null);
        Preconditions.checkArgument(joinDataLoader != null);
        Preconditions.checkArgument(keyFromJoinData != null);
        Preconditions.checkArgument(joinDataConverter != null);
        Preconditions.checkArgument(foundCallback != null);

        this.name = name;
        this.keyFromSourceData = keyFromSourceData;
        this.joinDataLoader = joinDataLoader;
        this.keyFromJoinData = keyFromJoinData;
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
    protected List<JOIN_DATA> getJoinDataByJoinKeys(List<JOIN_KEY> joinKeys) {
        return this.joinDataLoader.apply(joinKeys);
    }

    @Override
    protected JOIN_KEY createJoinKeyFromJoinData(JOIN_DATA joinData) {
        return this.keyFromJoinData.apply(joinData);
    }

    @Override
    protected RESULT convertToResult(JOIN_DATA joinData) {
        return this.joinDataConverter.apply(joinData);
    }

    @Override
    protected void onFound(SOURCE_DATA data, List<RESULT> JoinResults) {
        this.foundCallback.accept(data, JoinResults);
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
