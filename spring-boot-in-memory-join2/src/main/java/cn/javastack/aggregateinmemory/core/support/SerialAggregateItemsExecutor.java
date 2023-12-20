package cn.javastack.aggregateinmemory.core.support;

import cn.javastack.aggregateinmemory.core.AggregateItemExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by taoli on 2022/7/31.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 *
 * 串行执行器，多个 join 操作顺序执行
 */
@Slf4j
public class SerialAggregateItemsExecutor<DATA> extends AbstractAggregateItemsExecutor<DATA> {
    public SerialAggregateItemsExecutor(Class<DATA> dataCls,
                                        List<AggregateItemExecutor<DATA>> aggregateItemExecutors) {
        super(dataCls, aggregateItemExecutors);
    }

    @Override
    public void execute(List<DATA> datas) {
        List<AggregateItemExecutor<DATA>> aggregateItemExecutors = getAggregateItemExecutors();
        aggregateItemExecutors.forEach(dataAggregateExecutor -> {
            log.debug("run join on level {} use {}",
                    dataAggregateExecutor.runOnLevel(), dataAggregateExecutor);
            if (log.isDebugEnabled()){
                StopWatch stopWatch = StopWatch.createStarted();
                dataAggregateExecutor.execute(datas.get(0));
                stopWatch.stop();

                log.debug("run execute cost {} ms, executor is {}, data is {}.",
                        stopWatch.getTime(TimeUnit.MILLISECONDS),
                        dataAggregateExecutor,
                        datas);
            }else {
                dataAggregateExecutor.execute(datas.get(0));
            }

        });
    }
}
