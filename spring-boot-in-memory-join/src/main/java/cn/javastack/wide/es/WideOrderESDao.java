package cn.javastack.wide.es;

import cn.javastack.wide.WideOrder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Created by taoli on 2022/10/30.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
public interface WideOrderESDao extends ElasticsearchRepository<WideOrder, Long> {
    List<WideOrder> findByProductId(Long productId);

    List<WideOrder> findByAddressId(Long addressId);

    List<WideOrder> findByUserId(Long userId);
}
