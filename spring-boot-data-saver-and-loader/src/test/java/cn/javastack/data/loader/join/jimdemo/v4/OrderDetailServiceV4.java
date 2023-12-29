package cn.javastack.data.loader.join.jimdemo.v4;


import cn.javastack.data.loader.core.DataLoaderInMemoryService;
import cn.javastack.data.loader.join.jimdemo.service.OrderDetailService;
import cn.javastack.data.loader.join.jimdemo.service.order.Order;
import cn.javastack.data.loader.join.jimdemo.service.order.OrderRepository;
import cn.javastack.data.loader.join.jimdemo.vo.OrderDetailVO;
import cn.javastack.data.loader.join.jimdemo.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by taoli on 2022/7/30.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@Service
public class OrderDetailServiceV4 implements OrderDetailService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DataLoaderInMemoryService dataLoaderInMemoryService;

    @Override
    public List<? extends OrderDetailVO> getByUserId(Long userId) {
        List<Order> orders = this.orderRepository.getByUserId(userId);

        List<OrderDetailVOV4> orderDetailVOS = orders.stream()
                .map(order -> new OrderDetailVOV4(OrderVO.apply(order)))
                .collect(toList());

        this.dataLoaderInMemoryService.loaderInMemory(OrderDetailVOV4.class, orderDetailVOS);
        return orderDetailVOS;
    }
}
