package cn.javastack.domain.v5.service.order.impl;

import cn.javastack.domain.model.entity.Order;
import cn.javastack.domain.model.vo.*;
import cn.javastack.domain.repository.OrderRepository;
import cn.javastack.domain.service.OrderService;
import cn.javastack.domain.v5.model.vo.OrderDetailVOFetcherV2;
import cn.javastack.domain.v5.model.vo.OrderListVOFetcherV2;
import cn.javastack.domain.vo.*;
import cn.javastack.domain.v5.fetcher.FetcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class OrderServiceFetcherV2 implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private FetcherService fetcherService;

    @Override
    public List<OrderListVO> getByUserId(Long userId) {
        List<Order> orders = this.orderRepository.getByUserId(userId);

        List<OrderListVOFetcherV2> orderDetailVOS = orders.stream()
                .map(order -> new OrderListVOFetcherV2(OrderVO.apply(order)))
                .collect(toList());
        // VO 数据绑定发生变化，只需调整 VO 实现接口，此处无需变化
        fetcherService.fetch(OrderListVOFetcherV2.class, orderDetailVOS);

        return orderDetailVOS.stream()
                .collect(toList());
    }

    @Override
    public OrderDetailVO getDetailByOrderId(Long orderId) {
        Order order = this.orderRepository.getById(orderId);
        OrderDetailVOFetcherV2 orderDetail = new OrderDetailVOFetcherV2(OrderVO.apply(order));
        // VO 数据绑定发生变化，只需调整 VO 实现接口，此处无需变化
        fetcherService.fetch(OrderDetailVOFetcherV2.class, Arrays.asList(orderDetail));
        return orderDetail;
    }
}