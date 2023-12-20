package cn.javastack.demoProductDetail.infra.repository;

import cn.javastack.demoProductDetail.domain.service.ProductService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductServiceImplTest {
    @Resource
    private ProductService productServiceImplV6;

    @Resource
    private ProductService productServiceImplV1;

    @Test
    public void testLoad(){
        productServiceImplV6.loadProduct("016", "H1N100N6");
    }
}
