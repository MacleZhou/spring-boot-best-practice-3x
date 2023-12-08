package cn.javastack.domain.v6.service.order.impl;

import cn.javastack.domain.model.entity.Order;
import cn.javastack.domain.model.vo.OrderDetailVO;
import cn.javastack.domain.model.vo.OrderListVO;
import cn.javastack.domain.model.vo.OrderVO;
import cn.javastack.domain.repository.OrderRepository;
import cn.javastack.domain.service.OrderService;
import cn.javastack.domain.v6.fetcher.ConcurrentFetcherService;
import cn.javastack.domain.v6.model.vo.OrderDetailVOFetcherV3;
import cn.javastack.domain.v6.model.vo.OrderListVOFetcherV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class OrderServiceFetcherV3 implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ConcurrentFetcherService concurrentFetcherService;

    @Override
    public List<OrderListVO> getByUserId(Long userId) {
        List<Order> orders = this.orderRepository.getByUserId(userId);

        List<OrderListVOFetcherV3> orderDetailVOS = orders.stream()
                .map(order -> new OrderListVOFetcherV3(OrderVO.apply(order)))
                .collect(toList());

        // VO 数据绑定发生变化，只需调整 VO 实现接口，此处无需变化
        concurrentFetcherService.fetch(OrderListVOFetcherV3.class, orderDetailVOS);

        return orderDetailVOS.stream()
                .collect(toList());
    }

    @Override
    public OrderDetailVO getDetailByOrderId(Long orderId) {
        Order order = this.orderRepository.getById(orderId);
        OrderDetailVOFetcherV3 orderDetail = new OrderDetailVOFetcherV3(OrderVO.apply(order));
        // VO 数据绑定发生变化，只需调整 VO 实现接口，此处无需变化
        concurrentFetcherService.fetch(OrderDetailVOFetcherV3.class, Arrays.asList(orderDetail));
        return orderDetail;
    }
}