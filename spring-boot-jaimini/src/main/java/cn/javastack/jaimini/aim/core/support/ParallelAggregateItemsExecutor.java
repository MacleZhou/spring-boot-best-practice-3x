package cn.javastack.jaimini.aim.core.support;

import cn.javastack.jaimini.aim.core.AggregateItemExecutor;
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
 *
 * 并行执行器，同一 level 的 aggregate 在线程中并行执行
 */
@Slf4j
public class ParallelAggregateItemsExecutor<DATA> extends AbstractAggregateItemsExecutor<DATA> {
    private final ExecutorService executor;
    private final List<AggregateExecutorWithLevel> aggregateExecutorWithLevels;
    public ParallelAggregateItemsExecutor(Class<DATA> dataCls,
                                          List<AggregateItemExecutor<DATA>> aggregateItemExecutors,
                                          ExecutorService executor) {
        super(dataCls, aggregateItemExecutors);
        this.executor = executor;
        this.aggregateExecutorWithLevels = buildAggregateExecutorsWithLevel();
    }

    private List<AggregateExecutorWithLevel> buildAggregateExecutorsWithLevel() {
        List<AggregateExecutorWithLevel> collect = getAggregateItemExecutors().stream()
                .collect(Collectors.groupingBy(joinExecutor -> joinExecutor.runOnLevel()))
                .entrySet().stream()
                .map(entry -> new AggregateExecutorWithLevel(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        // 根据 level 进行排序，解决依赖问题
        Collections.sort(collect, Comparator.comparingInt(o -> o.level));
        return collect;
    }

    @Override
    public void execute(List<DATA> datas) {
        this.aggregateExecutorWithLevels.forEach(aggregateExecutorWithLevel -> {
            log.debug("run aggregate on level {} use {}",  aggregateExecutorWithLevel.getLevel(), aggregateExecutorWithLevel.getAggregateItemExecutors());
            List<Task> tasks = buildTasks(aggregateExecutorWithLevel, datas);
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

    private List<Task> buildTasks(AggregateExecutorWithLevel aggregateExecutorWithLevel, List<DATA> datas) {
        return aggregateExecutorWithLevel.getAggregateItemExecutors().stream()
                .map(joinExecutor -> new Task(joinExecutor, datas))
                .collect(Collectors.toList());
    }

    @Value
    class Task implements Callable<Void> {
        private final AggregateItemExecutor<DATA> aggregateItemExecutor;
        private final List<DATA> datas;

        @Override
        public Void call() throws Exception {
            this.aggregateItemExecutor.execute(this.datas);
            return null;
        }
    }

    @Value
    class AggregateExecutorWithLevel {
        private final Integer level;
        private final List<AggregateItemExecutor<DATA>> aggregateItemExecutors;
    }
}
