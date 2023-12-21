package cn.javastack.jaimini.aim.core.support;

import cn.javastack.jaimini.aim.core.AggregateItemExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * 串行执行器，多个 aggregate 操作顺序执行
 */
@Slf4j
public class SerialAggregateItemsExecutor<DATA> extends AbstractAggregateItemsExecutor<DATA> {
    public SerialAggregateItemsExecutor(Class<DATA> dataCls, List<AggregateItemExecutor<DATA>> joinItemExecutors) {
        super(dataCls, joinItemExecutors);
    }

    @Override
    public void execute(List<DATA> datas) {
        getAggregateItemExecutors().forEach(dataJoinExecutor -> {
            log.debug("run aggregate on level {} use {}",
                    dataJoinExecutor.runOnLevel(), dataJoinExecutor);
            if (log.isDebugEnabled()){
                StopWatch stopWatch = StopWatch.createStarted();
                dataJoinExecutor.execute(datas);
                stopWatch.stop();
                log.debug("run execute cost {} ms, executor is {}, data is {}.",
                        stopWatch.getTime(TimeUnit.MILLISECONDS),
                        dataJoinExecutor,
                        datas);
            }else {
                dataJoinExecutor.execute(datas);
            }
        });
    }
}
