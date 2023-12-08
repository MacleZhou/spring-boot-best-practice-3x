package cn.javastack.demoOrderDetail.v1;


import cn.javastack.demoOrderDetail.service.OrderDetailService;
import cn.javastack.demoOrderDetail.service.address.Address;
import cn.javastack.demoOrderDetail.service.address.AddressRepository;
import cn.javastack.demoOrderDetail.service.order.Order;
import cn.javastack.demoOrderDetail.service.order.OrderRepository;
import cn.javastack.demoOrderDetail.service.product.Product;
import cn.javastack.demoOrderDetail.service.product.ProductRepository;
import cn.javastack.demoOrderDetail.service.user.User;
import cn.javastack.demoOrderDetail.service.user.UserRepository;
import cn.javastack.demoOrderDetail.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public List<? extends OrderDetailVO> getByUserId(Long userId) {
        List<Order> orders = this.orderRepository.getByUserId(userId);
        return orders.stream()
                .map(order -> convertToOrderDetailVO(order))
                .collect(toList());
    }

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
