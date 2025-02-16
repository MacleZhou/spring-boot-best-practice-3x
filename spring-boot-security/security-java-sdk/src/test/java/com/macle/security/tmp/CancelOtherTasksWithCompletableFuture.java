package com.macle.security.tmp;

import java.util.concurrent.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CancelOtherTasksWithCompletableFuture {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(5); // 创建线程池
        List<CompletableFuture<?>> futures = IntStream.range(0, 30) // 创建3个任务
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    try {
                        Thread.sleep(1000 + (int) (Math.random() * 1000)); // 随机睡眠时间模拟耗时任务
                        System.out.println("Task " + i + " completed");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // 恢复中断状态（如果被取消的话）
                    }
                }, executor))
                .collect(Collectors.toList()); // 收集到列表中

        // 找到并取消其他任务（除了第一个完成的）
        futures.stream()
                .skip(1) // 跳过第一个完成的（如果有的话）
                .forEach(future -> future.cancel(true)); // 取消其他所有任务

        // 等待所有任务完成（理论上这里应该什么都不做，因为我们已经取消了其他任务）
        CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[0])).join(); // 等待所有任务完成（理论上这会立即返回，因为我们已经取消了）
        executor.shutdown(); // 关闭线程池
    }
}