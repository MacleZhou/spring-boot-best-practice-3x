package cn.javastack.data.loader.aggregate.aimtest;

import cn.javastack.data.loader.aggregate.aimdemo.domain.service.ProductService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
public class ProductServiceTest {

    @Resource
    private ProductService productServiceImplV1;

    @Resource
    private ProductService productServiceImplV4;
    @Resource
    private ProductService productServiceImplV41;

    @Resource
    private ProductService productServiceImplV42;

    @Resource
    private ProductService productServiceImplV6;

    @Resource
    private ProductService productServiceImplV61;

    @Test
    public void testV1(){
        productServiceImplV1.loadProduct("016", "H1N100N6");
    }
    @Test
    public void testV4_DataLoaderInMemory_Default(){
        productServiceImplV4.loadProduct("016", "H1N100N6");
    }

    @Test
    public void testV41_DataLoaderInMemory_Manual_SerialExecute(){
        productServiceImplV41.loadProduct("016", "H1N100N6");
    }

    @Test
    public void testV42_DataLoaderInMemory_Manual_ExecutorService(){
        productServiceImplV42.loadProduct("016", "H1N100N6");
    }

    @Test
    public void testV6ParallelAggregate(){
        productServiceImplV6.loadProduct("016", "H1N100N6");
    }

    @Test
    public void testV61_No_need_converter(){
        productServiceImplV61.loadProduct("016", "H1N100N6");
    }

    @Test
    public void testPerformance(){
        StopWatch stopWatch1 = StopWatch.createStarted();
        productServiceImplV1.loadProduct("016", "H1N100N6");
        stopWatch1.stop();

        StopWatch stopWatch4 = StopWatch.createStarted();
        productServiceImplV4.loadProduct("016", "H1N100N6");
        stopWatch4.stop();

        StopWatch stopWatch6 = StopWatch.createStarted();
        productServiceImplV6.loadProduct("016", "H1N100N6");
        stopWatch6.stop();

        log.info("Performance: V1={}, V4={}, V6={}", stopWatch1.getTime(TimeUnit.MILLISECONDS), stopWatch4.getTime(TimeUnit.MILLISECONDS), stopWatch6.getTime(TimeUnit.MILLISECONDS));
    }
}
