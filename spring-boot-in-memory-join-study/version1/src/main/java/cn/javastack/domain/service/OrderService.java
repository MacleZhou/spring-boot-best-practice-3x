package cn.javastack.domain.service;

import cn.javastack.domain.model.vo.OrderListVO;
import cn.javastack.domain.model.vo.OrderDetailVO;

import java.util.List;

public interface OrderService {
    // 我的订单
    List<OrderListVO> getByUserId(Long userId);
    // 订单详情
    OrderDetailVO getDetailByOrderId(Long orderId);
}