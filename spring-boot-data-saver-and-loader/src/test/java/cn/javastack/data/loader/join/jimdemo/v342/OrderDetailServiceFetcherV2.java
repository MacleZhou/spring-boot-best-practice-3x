package cn.javastack.data.loader.join.jimdemo.v342;


import cn.javastack.data.loader.join.jimdemo.service.OrderDetailService;
import cn.javastack.data.loader.join.jimdemo.service.order.Order;
import cn.javastack.data.loader.join.jimdemo.service.order.OrderRepository;
import cn.javastack.data.loader.join.jimdemo.v342.core.ConcurrentFetcherService;
import cn.javastack.data.loader.join.jimdemo.v342.core.FetcherService;
import cn.javastack.data.loader.join.jimdemo.v342.fetcher.OrderDetailVOFetcherV2;
import cn.javastack.data.loader.join.jimdemo.vo.OrderDetailVO;
import cn.javastack.data.loader.join.jimdemo.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service("orderDetailServiceFetcherV2")
public class OrderDetailServiceFetcherV2 implements OrderDetailService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private FetcherService fetcherService;

    @Autowired
    private ConcurrentFetcherService concurrentFetcherService;

    @Override
    public List<? extends OrderDetailVO> getByUserId(Long userId) {
        List<Order> orders = this.orderRepository.getByUserId(userId);
        if (CollectionUtils.isEmpty(orders)) {
            return null;
        }
        List<OrderDetailVOFetcherV2> orderDetailVOS = orders.stream()
                .map(order -> new OrderDetailVOFetcherV2(OrderVO.apply(order)))
                .collect(toList());

        // VO 数据绑定发生变化，只需调整 VO 实现接口，此处无需变化
        fetcherService.fetch(OrderDetailVOFetcherV2.class, orderDetailVOS);

        return orderDetailVOS.stream()
                .collect(toList());
    }

    public List<? extends OrderDetailVO> getByUserIdConcurrent(Long userId) {
        List<Order> orders = this.orderRepository.getByUserId(userId);
        if (CollectionUtils.isEmpty(orders)) {
            return null;
        }
        List<OrderDetailVOFetcherV2> orderDetailVOS = orders.stream()
                .map(order -> new OrderDetailVOFetcherV2(OrderVO.apply(order)))
                .collect(toList());

        // VO 数据绑定发生变化，只需调整 VO 实现接口，此处无需变化
        concurrentFetcherService.fetch(OrderDetailVOFetcherV2.class, orderDetailVOS);

        return orderDetailVOS.stream()
                .collect(toList());
    }
}