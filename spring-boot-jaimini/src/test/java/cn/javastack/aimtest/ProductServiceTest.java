package cn.javastack.aimtest;

import cn.javastack.aimdemo.domain.service.ProductService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class ProductServiceTest {

    @Resource
    private ProductService productServiceImplV1;

    @Resource
    private ProductService productServiceImplV4;

    @Resource
    private ProductService productServiceImplV6;

    @Test
    public void testV1(){
        productServiceImplV1.loadProduct("016", "H1N100N6");
    }
    @Test
    public void testV4(){
        productServiceImplV4.loadProduct("016", "H1N100N6");
    }

    @Test
    public void testV6ParallelAggregate(){
        productServiceImplV6.loadProduct("016", "H1N100N6");
    }
}
