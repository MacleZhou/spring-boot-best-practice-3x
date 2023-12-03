package cn.javastack.domain.v3.order.impl;

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
import com.google.common.collect.Lists;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
public class OrderServiceCodingV3 implements OrderService {
    private ExecutorService executorService;

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

    @PostConstruct
    public void init(){
        // 初始化线程池（不要使用Executors，这里只是演示，需要对资源进行评估）
        this.executorService = Executors.newFixedThreadPool(20);
    }

    @SneakyThrows
    @Override
    public List<OrderListVO> getByUserId(Long userId) {
        List<Order> orders = this.orderRepository.getByUserId(userId);

        List<OrderListVOCodingV2> orderDetailVOS = orders.stream()
                .map(order -> new OrderListVOCodingV2(OrderVO.apply(order)))
                .collect(toList());

        List<Callable<Void>> callables = Lists.newArrayListWithCapacity(3);
        // 创建异步任务
        callables.add(() -> {
            // 批量获取用户，并依次进行绑定
            List<Long> userIds = orders.stream()
                    .map(Order::getUserId)
                    .collect(toList());
            List<User> users = this.userRepository.getByIds(userIds);
            Map<Long, User> userMap = users.stream()
                    .collect(toMap(User::getId, Function.identity(), (a, b) -> a));
            for (OrderListVOCodingV2 orderDetailVO : orderDetailVOS){
                User user = userMap.get(orderDetailVO.getOrder().getUserId());
                UserVO userVO = UserVO.apply(user);
                orderDetailVO.setUser(userVO);
            }
            return null;
        });
        // 创建异步任务
        callables.add(() ->{
            // 批量获取地址，并依次进行绑定
            List<Long> addressIds = orders.stream()
                    .map(Order::getAddressId)
                    .collect(toList());
            List<Address> addresses = this.addressRepository.getByIds(addressIds);
            Map<Long, Address> addressMap = addresses.stream()
                    .collect(toMap(Address::getId, Function.identity(), (a, b) -> a));
            for (OrderListVOCodingV2 orderDetailVO : orderDetailVOS){
                Address address = addressMap.get(orderDetailVO.getOrder().getAddressId());
                AddressVO addressVO = AddressVO.apply(address);
                orderDetailVO.setAddress(addressVO);
            }
            return null;
        });
        // 创建异步任务
        callables.add(() -> {
            // 批量获取商品，并依次进行绑定
            List<Long> productIds = orders.stream()
                    .map(Order::getProductId)
                    .collect(toList());
            List<Product> products = this.productRepository.getByIds(productIds);
            Map<Long, Product> productMap = products.stream()
                    .collect(toMap(Product::getId, Function.identity(), (a, b) -> a));
            for (OrderListVOCodingV2 orderDetailVO : orderDetailVOS){
                Product product = productMap.get(orderDetailVO.getOrder().getProductId());
                ProductVO productVO = ProductVO.apply(product);
                orderDetailVO.setProduct(productVO);
            }
            return null;
        });

        // 执行异步任务
        this.executorService.invokeAll(callables);

        return orderDetailVOS.stream()
                .collect(toList());
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
        orderDetail.setPayInfo(PayInfoVO.apply(this.payInfoRepository.getByOrderId(order.getId())));
        return orderDetail;
    }
}