package cn.javastack.joininmemory.core.support;

import cn.javastack.joininmemory.core.JoinItemExecutor;
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
public class SerialJoinItemsExecutor<DATA> extends AbstractJoinItemsExecutor<DATA> {
    public SerialJoinItemsExecutor(Class<DATA> dataCls,
                                   List<JoinItemExecutor<DATA>> joinItemExecutors) {
        super(dataCls, joinItemExecutors);
        log.debug("jim.SerialJoinItemsExecutor.new {}, and joinItemExecutors={}", dataCls.getCanonicalName(), joinItemExecutors.size());
    }

    @Override
    public void execute(List<DATA> datas) {
        log.debug("jim.SerialJoinItemsExecutor.execute");
        List<JoinItemExecutor<DATA>> joinItemExecutors = getJoinItemExecutors();
        log.debug("jim.SerialJoinItemsExecutor.execute.size=" + joinItemExecutors.size() + "; isDebugEnabled=" + log.isDebugEnabled() + ";datas=" + datas.size());
        joinItemExecutors.forEach(dataJoinExecutor -> {
            log.debug("jim.SerialJoinItemsExecutor.run join on level {} use {}",
                    dataJoinExecutor.runOnLevel(), dataJoinExecutor);
            if (log.isDebugEnabled()){
                StopWatch stopWatch = StopWatch.createStarted();
                dataJoinExecutor.execute(datas);
                stopWatch.stop();

                log.debug("jim.SerialJoinItemsExecutor.run execute cost {} ms, executor is {}, data is {}.",
                        stopWatch.getTime(TimeUnit.MILLISECONDS),
                        dataJoinExecutor,
                        datas);
            }else {
                log.debug("jim.SerialJoinItemsExecutor.execute.else.begin");
                dataJoinExecutor.execute(datas);
                log.debug("jim.SerialJoinItemsExecutor.execute.else.end");
            }

        });
    }
}
