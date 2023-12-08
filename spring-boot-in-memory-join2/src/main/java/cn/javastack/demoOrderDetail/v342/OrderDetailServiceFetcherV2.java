package cn.javastack.demoOrderDetail.v342;


import cn.javastack.demoOrderDetail.service.OrderDetailService;
import cn.javastack.demoOrderDetail.service.order.Order;
import cn.javastack.demoOrderDetail.service.order.OrderRepository;
import cn.javastack.demoOrderDetail.v342.fetcher.AddressVOFetcherExecutorV2;
import cn.javastack.demoOrderDetail.v342.fetcher.OrderDetailVOFetcherV2;
import cn.javastack.demoOrderDetail.v342.fetcher.ProductVOFetcherExecutorV2;
import cn.javastack.demoOrderDetail.v342.fetcher.UserVOFetcherExecutorV2;
import cn.javastack.demoOrderDetail.vo.OrderDetailVO;
import cn.javastack.demoOrderDetail.vo.OrderVO;
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
    private AddressVOFetcherExecutorV2 addressVOFetcherExecutorV2;
    @Autowired
    private ProductVOFetcherExecutorV2 productVOFetcherExecutorV2;
    @Autowired
    private UserVOFetcherExecutorV2 userVOFetcherExecutorV2;


    @Override
    public List<? extends OrderDetailVO> getByUserId(Long userId) {
        List<Order> orders = this.orderRepository.getByUserId(userId);
        if (CollectionUtils.isEmpty(orders)) {
            return null;
        }
        List<OrderDetailVOFetcherV2> orderDetailVOS = orders.stream()
                .map(order -> new OrderDetailVOFetcherV2(OrderVO.apply(order)))
                .collect(toList());
        // 直接使用 FetcherExecutor 完成数据绑定
        this.addressVOFetcherExecutorV2.fetch(orderDetailVOS);
        this.productVOFetcherExecutorV2.fetch(orderDetailVOS);
        this.userVOFetcherExecutorV2.fetch(orderDetailVOS);

        return orderDetailVOS.stream()
                .collect(toList());
    }
}