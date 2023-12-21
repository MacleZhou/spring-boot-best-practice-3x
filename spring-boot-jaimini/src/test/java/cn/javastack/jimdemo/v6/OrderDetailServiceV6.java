package cn.javastack.jimdemo.v6;


import cn.javastack.jaimini.jim.core.JoinService;
import cn.javastack.jimdemo.service.OrderDetailService;
import cn.javastack.jimdemo.service.order.Order;
import cn.javastack.jimdemo.service.order.OrderRepository;
import cn.javastack.jimdemo.vo.OrderDetailVO;
import cn.javastack.jimdemo.vo.OrderVO;
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
public class OrderDetailServiceV6 implements OrderDetailService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private JoinService joinService;

    @Override
    public List<? extends OrderDetailVO> getByUserId(Long userId) {
        List<Order> orders = this.orderRepository.getByUserId(userId);

        List<OrderDetailVOV6> orderDetailVOS = orders.stream()
                .map(order -> new OrderDetailVOV6(OrderVO.apply(order)))
                .collect(toList());

        this.joinService.joinInMemory(OrderDetailVOV6.class, orderDetailVOS);
        return orderDetailVOS;
    }
}
