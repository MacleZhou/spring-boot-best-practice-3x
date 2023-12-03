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

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by taoli on 2022/7/30.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@Service
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
