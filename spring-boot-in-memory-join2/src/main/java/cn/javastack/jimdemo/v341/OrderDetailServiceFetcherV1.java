package cn.javastack.jimdemo.v341;


import cn.javastack.jimdemo.service.OrderDetailService;
import cn.javastack.jimdemo.service.order.Order;
import cn.javastack.jimdemo.service.order.OrderRepository;
import cn.javastack.jimdemo.v341.fetcher.AddressVOFetcherExecutorV1;
import cn.javastack.jimdemo.v341.fetcher.OrderDetailVOFetcherV1;
import cn.javastack.jimdemo.v341.fetcher.ProductVOFetcherExecutorV1;
import cn.javastack.jimdemo.v341.fetcher.UserVOFetcherExecutorV1;
import cn.javastack.jimdemo.vo.OrderDetailVO;
import cn.javastack.jimdemo.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service("orderDetailServiceFetcherV1")
public class OrderDetailServiceFetcherV1 implements OrderDetailService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AddressVOFetcherExecutorV1 addressVOFetcherExecutorV1;
    @Autowired
    private ProductVOFetcherExecutorV1 productVOFetcherExecutorV1;
    @Autowired
    private UserVOFetcherExecutorV1 userVOFetcherExecutorV1;


    @Override
    public List<? extends OrderDetailVO> getByUserId(Long userId) {
        List<Order> orders = this.orderRepository.getByUserId(userId);
        if (CollectionUtils.isEmpty(orders)) {
            return null;
        }
        List<OrderDetailVOFetcherV1> orderDetailVOS = orders.stream()
                .map(order -> new OrderDetailVOFetcherV1(OrderVO.apply(order)))
                .collect(toList());
        // 直接使用 FetcherExecutor 完成数据绑定
        this.addressVOFetcherExecutorV1.fetch(orderDetailVOS);
        this.productVOFetcherExecutorV1.fetch(orderDetailVOS);
        this.userVOFetcherExecutorV1.fetch(orderDetailVOS);

        return orderDetailVOS.stream()
                .collect(toList());
    }
}