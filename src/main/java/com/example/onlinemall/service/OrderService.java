package com.example.onlinemall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.onlinemall.dto.OrderResponse;
import com.example.onlinemall.entity.Order;

import java.util.List;

public interface OrderService extends IService<Order> {

    /**
     * 从购物车创建订单
     * @param userId 用户ID
     * @return 创建的订单信息
     */
    OrderResponse createOrderFromCart(Long userId);

    /**
     * 获取用户的订单列表
     * @param userId 用户ID
     * @return 订单列表
     */
    List<OrderResponse> getUserOrders(Long userId);
}