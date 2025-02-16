package com.macle.security.tmp;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class ConcurrentSearchExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 创建一个固定大小的线程池
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // 定义一个搜索任务，这里我们模拟一个耗时的搜索操作
        Supplier<String> task1 = () -> {
            // 模拟耗时操作
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "Result from Task 1";
        };

        Supplier<String> task2 = () -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "Result from Task 2";
        };

        // 使用CompletableFuture提交任务，并设置其中一个任务完成时其他任务取消的逻辑
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(task1, executor)
                .thenApply(result -> {
                    System.out.println("Task 1 completed: " + result);
                    return result; // 直接返回结果，不需要进一步处理
                });
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(task2, executor)
                .thenApply(result -> {
                    System.out.println("Task 2 completed: " + result);
                    return result; // 直接返回结果，不需要进一步处理
                });

        // 使用anyOf来等待任意一个任务完成，并获取结果。anyOf会取消未完成的任务。
        CompletableFuture<Object> anyResult = CompletableFuture.anyOf(future1, future2);
        String result = (String) anyResult.get(); // 获取任意一个任务的结果
        System.out.println("Final Result: " + result);

        // 关闭线程池
        executor.shutdown();
    }
}