package cn.javastack.demo.v5;


import cn.javastack.demo.service.OrderDetailService;
import cn.javastack.demo.service.order.Order;
import cn.javastack.demo.service.order.OrderRepository;
import cn.javastack.demo.vo.OrderDetailVO;
import cn.javastack.demo.vo.OrderVO;
import cn.javastack.joininmemory.core.JoinService;
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
public class OrderDetailServiceV5 implements OrderDetailService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private JoinService joinService;

    @Override
    public List<? extends OrderDetailVO> getByUserId(Long userId) {
        List<Order> orders = this.orderRepository.getByUserId(userId);

        List<OrderDetailVOV5> orderDetailVOS = orders.stream()
                .map(order -> new OrderDetailVOV5(OrderVO.apply(order)))
                .collect(toList());

        this.joinService.joinInMemory(OrderDetailVOV5.class, orderDetailVOS);
        return orderDetailVOS;
    }
}
