package cn.javastack.domain.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class ThreadPoolTest {
    @Test
    public void testByExecutorService() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<String> user = executorService.submit(new GetUserTask());
        Future<String> order = executorService.submit(new GetOrderTask());
        String userInfo = user.get();//Block to get the user info
        String orderInfo = order.get();//Block to get the order info
        log.info(userInfo + orderInfo);
    }

    @Test
    public void testByExecutorServiceIfException() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<String> user = executorService.submit(new GetUserTask());
        Future<String> order = executorService.submit(new GetOrderTaskThrowException());
        String userInfo = user.get();//Block to get the user info
        String orderInfo = order.get();//Block to get the order info
        log.info(userInfo + orderInfo);
    }

    /** JDK19
    @Test
    public void testByShutdownOnFailure() throws ExecutionException, InterruptedException {
        StructuredTaskScope.ShutdownOnFailure scope = new StructuredTaskScope.ShutdownOnFailure<>();
        Supplier<String> user = scope.fork(new GetUserTask());
        Supplier<String> order = scope.fork(new GetOrderTaskThrowException());
        scope.join();
        String userInfo = user.get();//Block to get the user info
        String orderInfo = order.get();//Block to get the order info
        log.info(userInfo + orderInfo);
    }*/

    /** JDK19
     @Test
     public void testByShutdownOnSuccess() throws ExecutionException, InterruptedException {
     StructuredTaskScope.ShutdownOnSuccess scope = new StructuredTaskScope.ShutdownOnSuccess<>();
     Supplier<String> user = scope.fork(new GetUserTask());
     Supplier<String> order = scope.fork(new GetOrderTask());
     scope.join();
     String userInfo = user.get();//Block to get the user info
     String orderInfo = order.get();//Block to get the order info
     log.info(userInfo + orderInfo);
     }*/
}
