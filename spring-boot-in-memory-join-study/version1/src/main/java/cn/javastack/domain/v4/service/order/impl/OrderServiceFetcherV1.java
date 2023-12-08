package cn.javastack.domain.v4.service.order.impl;

import cn.javastack.domain.model.entity.Order;
import cn.javastack.domain.model.vo.*;
import cn.javastack.domain.v4.fetcher.AddressVOFetcherExecutorV1;
import cn.javastack.domain.v4.fetcher.PayInfoVOFetcherExecutorV1;
import cn.javastack.domain.v4.fetcher.ProductVOFetcherExecutorV1;
import cn.javastack.domain.v4.fetcher.UserVOFetcherExecutorV1;
import cn.javastack.domain.repository.OrderRepository;
import cn.javastack.domain.service.OrderService;
import cn.javastack.domain.v4.model.vo.OrderDetailVOFetcherV1;
import cn.javastack.domain.v4.model.vo.OrderListVOFetcherV1;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class OrderServiceFetcherV1 implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AddressVOFetcherExecutorV1 addressVOFetcherExecutorV1;
    @Autowired
    private ProductVOFetcherExecutorV1 productVOFetcherExecutorV1;
    @Autowired
    private UserVOFetcherExecutorV1 userVOFetcherExecutorV1;
    @Autowired
    private PayInfoVOFetcherExecutorV1 payInfoVOFetcherExecutorV1;

    @Override
    public List<OrderListVO> getByUserId(Long userId) {
        List<Order> orders = this.orderRepository.getByUserId(userId);

        List<OrderListVOFetcherV1> orderDetailVOS = orders.stream()
                .map(order -> new OrderListVOFetcherV1(OrderVO.apply(order)))
                .collect(toList());
        // 直接使用 FetcherExecutor 完成数据绑定
        this.addressVOFetcherExecutorV1.fetch(orderDetailVOS);
        this.productVOFetcherExecutorV1.fetch(orderDetailVOS);
        this.userVOFetcherExecutorV1.fetch(orderDetailVOS);

        return orderDetailVOS.stream()
                .collect(toList());
    }

    @Override
    public OrderDetailVO getDetailByOrderId(Long orderId) {
        Order order = this.orderRepository.getById(orderId);
        OrderDetailVOFetcherV1 orderDetail = new OrderDetailVOFetcherV1(OrderVO.apply(order));
        List<OrderDetailVOFetcherV1> orderDetailVOS = Arrays.asList(orderDetail);
        // 直接使用 FetcherExecutor 完成数据绑定
        this.addressVOFetcherExecutorV1.fetch(orderDetailVOS);
        this.productVOFetcherExecutorV1.fetch(orderDetailVOS);
        this.userVOFetcherExecutorV1.fetch(orderDetailVOS);
        this.payInfoVOFetcherExecutorV1.fetch(orderDetailVOS);
        return orderDetail;
    }
}