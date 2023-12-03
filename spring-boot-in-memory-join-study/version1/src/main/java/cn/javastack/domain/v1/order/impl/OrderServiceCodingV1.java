package cn.javastack.domain.v1.order.impl;

import cn.javastack.domain.entity.*;
import cn.javastack.domain.model.entity.Address;
import cn.javastack.domain.model.entity.Order;
import cn.javastack.domain.model.entity.Product;
import cn.javastack.domain.model.entity.User;
import cn.javastack.domain.model.vo.*;
import cn.javastack.domain.repository.AddressRepository;
import cn.javastack.domain.repository.OrderRepository;
import cn.javastack.domain.service.OrderService;
import cn.javastack.domain.repository.PayInfoRepository;
import cn.javastack.domain.repository.ProductRepository;
import cn.javastack.domain.repository.UserRepository;
import cn.javastack.domain.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class OrderServiceCodingV1 implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PayInfoRepository payInfoRepository;

    @Override
    public List<OrderListVO> getByUserId(Long userId) {
        // 获取用户订单
        List<Order> orders = this.orderRepository.getByUserId(userId);
        // 依次进行数据绑定
        return orders.stream()
                .map(order -> convertToOrderListVO(order))
                .collect(toList());
    }

    private OrderListVOCodingV1 convertToOrderListVO(Order order) {
        OrderVO orderVO = OrderVO.apply(order);

        OrderListVOCodingV1 orderDetailVO = new OrderListVOCodingV1(orderVO);
        // 绑定地址信息
        Address address = this.addressRepository.getById(order.getAddressId());
        AddressVO addressVO = AddressVO.apply(address);
        orderDetailVO.setAddress(addressVO);
        // 绑定用户信息
        User user = this.userRepository.getById(order.getUserId());
        UserVO userVO = UserVO.apply(user);
        orderDetailVO.setUser(userVO);
        // 绑定商品信息
        Product product = this.productRepository.getById(order.getProductId());
        ProductVO productVO = ProductVO.apply(product);
        orderDetailVO.setProduct(productVO);

        return orderDetailVO;
    }

    @Override
    public OrderDetailVO getDetailByOrderId(Long orderId) {
        // 暂时忽略
        Order order = this.orderRepository.getById(orderId);
        return convertToOrderDetailVO(order);
    }

    private OrderDetailVO convertToOrderDetailVO(Order order) {
        OrderDetailVOCodingV1 orderDetail = new OrderDetailVOCodingV1(OrderVO.apply(order));
        // 获取地址并进行绑定
        Address address = this.addressRepository.getById(order.getAddressId());
        AddressVO addressVO = AddressVO.apply(address);
        orderDetail.setAddress(addressVO);
        // 获取用户并进行绑定
        User user = this.userRepository.getById(order.getUserId());
        UserVO userVO = UserVO.apply(user);
        orderDetail.setUser(userVO);
        // 获取商品并进行绑定
        Product product = this.productRepository.getById(order.getProductId());
        ProductVO productVO = ProductVO.apply(product);
        orderDetail.setProduct(productVO);
        // 获取支付信息并进行绑定
        /*
        List<PayInfo> payInfos = this.payInfoRepository.getByOrderId(order.getId());
        List<PayInfoVO> payInfoVOList = payInfos.stream()
                .map(PayInfoVO::apply)
                .collect(toList());*/
        orderDetail.setPayInfo(PayInfoVO.apply(this.payInfoRepository.getByOrderId(order.getId())));
        return orderDetail;
    }

}