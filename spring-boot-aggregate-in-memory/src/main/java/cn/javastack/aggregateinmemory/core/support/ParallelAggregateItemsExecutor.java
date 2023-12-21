package cn.javastack.aggregateinmemory.core.support;

import cn.javastack.aggregateinmemory.core.AggregateItemExecutor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by taoli on 2022/7/31.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 *
 * 并行执行器，同一 level 的 join 在线程中并行执行
 */
@Slf4j
public class ParallelAggregateItemsExecutor<DATA> extends AbstractAggregateItemsExecutor<DATA> {
    private final ExecutorService executor;
    private final List<JoinExecutorWithLevel> joinExecutorWithLevels;
    public ParallelAggregateItemsExecutor(Class<DATA> dataCls,
                                          List<AggregateItemExecutor<DATA>> joinItemExecutors,
                                          ExecutorService executor) {
        super(dataCls, joinItemExecutors);
        this.executor = executor;
        this.joinExecutorWithLevels = buildJoinExecutorWithLevel();
    }

    private List<JoinExecutorWithLevel> buildJoinExecutorWithLevel() {
        List<JoinExecutorWithLevel> collect = getJoinItemExecutors().stream()
                .collect(Collectors.groupingBy(joinExecutor -> joinExecutor.runOnLevel()))
                .entrySet().stream()
                .map(entry -> new JoinExecutorWithLevel(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        // 根据 level 进行排序，解决依赖问题
        Collections.sort(collect, Comparator.comparingInt(o -> o.level));
        return collect;
    }

    @Override
    public void execute(List<DATA> datas) {
        this.joinExecutorWithLevels.forEach(joinExecutorWithLevel -> {
            log.debug("run join on level {} use {}",  joinExecutorWithLevel.getLevel(), joinExecutorWithLevel.getJoinItemExecutors());
            List<Task> tasks = buildTasks(joinExecutorWithLevel, datas);
            try {
                if (log.isDebugEnabled()) {
                    StopWatch stopWatch = StopWatch.createStarted();
                    this.executor.invokeAll(tasks);
                    stopWatch.stop();
                    log.debug("run execute cost {} ms, task is {}.", stopWatch.getTime(TimeUnit.MILLISECONDS), tasks);
                }else {
                    this.executor.invokeAll(tasks);
                }
            } catch (InterruptedException e) {
                log.error("invoke task {} interrupted", tasks, e);
            }
        });
    }

    private List<Task> buildTasks(JoinExecutorWithLevel joinExecutorWithLevel, List<DATA> datas) {
        return joinExecutorWithLevel.getJoinItemExecutors().stream()
                .map(joinExecutor -> new Task(joinExecutor, datas))
                .collect(Collectors.toList());
    }

    @Value
    class Task implements Callable<Void> {
        private final AggregateItemExecutor<DATA> joinItemExecutor;
        private final List<DATA> datas;

        @Override
        public Void call() throws Exception {
            this.joinItemExecutor.execute(this.datas);
            return null;
        }
    }

    @Value
    class JoinExecutorWithLevel{
        private final Integer level;
        private final List<AggregateItemExecutor<DATA>> joinItemExecutors;
    }
}
