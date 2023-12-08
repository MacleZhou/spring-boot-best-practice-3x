package cn.javastack.demoOrderDetail.service.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static cn.javastack.demoOrderDetail.utils.TimeUtils.sleepAsMS;
import static java.util.stream.Collectors.toList;

/**
 * Created by taoli on 2022/7/30.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@Repository
@Slf4j
public class ProductRepository {

    public List<Product> getByIds(List<Long> ids){
        sleepAsMS(10);
        return ids.stream()
                .map(id -> createProduct(id))
                .collect(toList());
    }

    public Product getById(Long id){
        sleepAsMS(3);
        log.info("Get Product By Id {}", id);
        return createProduct(id);
    }

    private Product createProduct(Long id) {
        return Product.builder()
                .id(id)
                .name("商品-" + id)
                .price(id.intValue() / 100)
                .build();
    }
}
