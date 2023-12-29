package cn.javastack.data.loader.core.support;

import cn.javastack.data.loader.annotation.DataHolderType;
import cn.javastack.data.loader.core.DataItemExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * 串行执行器，多个 join 操作顺序执行
 */
@Slf4j
public class SerialDataItemsExecutor<DATA> extends AbstractDataItemsExecutor<DATA> {
    public SerialDataItemsExecutor(Class<DATA> dataCls, DataHolderType dataHolderType,
                                   List<DataItemExecutor<DATA>> dataItemExecutors) {
        super(dataCls, dataHolderType, dataItemExecutors);
    }

    @Override
    public void execute(List<DATA> datas) {
        getDataItemExecutors().forEach(dataJoinExecutor -> {
            log.debug("run join on level {} use {}",
                    dataJoinExecutor.runOnLevel(), dataJoinExecutor);
            if (log.isDebugEnabled()){
                StopWatch stopWatch = StopWatch.createStarted();
                dataJoinExecutor.execute(datas, super.getDataHolderType());
                stopWatch.stop();

                log.debug("run execute cost {} ms, executor is {}, data is {}.",
                        stopWatch.getTime(TimeUnit.MILLISECONDS),
                        dataJoinExecutor,
                        datas);
            }else {
                dataJoinExecutor.execute(datas, super.getDataHolderType());
            }

        });
    }
}
