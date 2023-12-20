package cn.javastack.demo.v1;


import cn.javastack.demo.service.OrderDetailService;
import cn.javastack.demo.service.address.Address;
import cn.javastack.demo.service.address.AddressRepository;
import cn.javastack.demo.service.order.Order;
import cn.javastack.demo.service.order.OrderRepository;
import cn.javastack.demo.service.product.Product;
import cn.javastack.demo.service.product.ProductRepository;
import cn.javastack.demo.service.user.User;
import cn.javastack.demo.service.user.UserRepository;
import cn.javastack.demo.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by taoli on 2022/7/30.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@Service("orderDetailServiceV1")
public class OrderDetailServiceV1 implements OrderDetailService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 数据库IO : 总数据库连接次数等于 1 | 1 + n * 3 (n=1中返回的订单总量)
     * Time : 所有的累加 1 + n * ( a + p + u)
     */
    @Override
    public List<? extends OrderDetailVO> getByUserId(Long userId) {
        List<Order> orders = this.orderRepository.getByUserId(userId);
        if (CollectionUtils.isEmpty(orders)) {
            return null;
        }
        return orders.stream()
                .map(order -> convertToOrderDetailVO(order))
                .collect(toList());

    }

    /**
     * 按照每个订单进行订单的关联数据拉取，当订单数量多师，则对数据库造成很大压力，因为不确定数据库连接次数
     */
    private OrderDetailVOV1 convertToOrderDetailVO(Order order) {
        OrderVO orderVO = OrderVO.apply(order);

        OrderDetailVOV1 orderDetailVO = new OrderDetailVOV1(orderVO);

        Address address = this.addressRepository.getById(order.getAddressId());
        AddressVO addressVO = AddressVO.apply(address);
        orderDetailVO.setAddress(addressVO);

        User user = this.userRepository.getById(order.getUserId());
        UserVO userVO = UserVO.apply(user);
        orderDetailVO.setUser(userVO);

        Product product = this.productRepository.getById(order.getProductId());
        ProductVO productVO = ProductVO.apply(product);
        orderDetailVO.setProduct(productVO);

        return orderDetailVO;
    }
}
