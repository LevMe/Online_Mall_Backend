package com.example.onlinemall.controller;

import com.example.onlinemall.common.Result;
import com.example.onlinemall.dto.OrderResponse;
import com.example.onlinemall.interceptor.JwtInterceptor;
import com.example.onlinemall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Result<OrderResponse> createOrder() {
        Long userId = JwtInterceptor.getCurrentUserId();
        OrderResponse order = orderService.createOrderFromCart(userId);
        return Result.created(order, "订单创建成功");
    }

    @GetMapping
    public Result<List<OrderResponse>> getOrders() {
        Long userId = JwtInterceptor.getCurrentUserId();
        List<OrderResponse> orders = orderService.getUserOrders(userId);
        return Result.success(orders, "获取成功");
    }
}