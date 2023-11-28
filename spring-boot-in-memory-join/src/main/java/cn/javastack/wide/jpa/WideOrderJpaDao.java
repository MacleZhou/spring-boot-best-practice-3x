package cn.javastack.wide.jpa;

import cn.javastack.wide.WideOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by taoli on 2022/10/30.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
public interface WideOrderJpaDao extends JpaRepository<WideOrder, Long> {
    List<WideOrder> findByProductId(Long productId);

    List<WideOrder> findByAddressId(Long addressId);

    List<WideOrder> findByUserId(Long userId);
}
