package com.example.onlinemall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.onlinemall.common.BusinessException;
import com.example.onlinemall.dto.OrderResponse;
import com.example.onlinemall.entity.Order;
import com.example.onlinemall.entity.OrderItem;
import com.example.onlinemall.entity.Product;
import com.example.onlinemall.mapper.OrderItemMapper;
import com.example.onlinemall.mapper.OrderMapper;
import com.example.onlinemall.service.CartItemService;
import com.example.onlinemall.service.OrderService;
import com.example.onlinemall.service.ProductService;
import com.example.onlinemall.vo.CartItemDetailVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    @Transactional // 开启事务，确保订单创建过程的原子性
    public OrderResponse createOrderFromCart(Long userId) {
        // 1. 获取用户的购物车详情
        List<CartItemDetailVO> cartItems = cartItemService.viewCart(userId).getItems();
        if (cartItems.isEmpty()) {
            throw new BusinessException(400, "购物车为空，无法创建订单");
        }

        // 2. 创建订单主表记录
        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setUserId(userId);
        // 计算总价
        BigDecimal totalAmount = cartItems.stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(totalAmount);
        order.setStatus("PAID"); // 模拟支付成功
        this.baseMapper.insert(order);

        // 3. 遍历购物车项，创建订单项并扣减库存
        List<OrderResponse.OrderItemInfo> orderItemInfos = new ArrayList<>();
        for (CartItemDetailVO cartItem : cartItems) {
            // 校验库存
            Product product = productService.getById(cartItem.getProductId());
            if (product.getStock() < cartItem.getQuantity()) {
                throw new BusinessException(400, "商品 \"" + product.getName() + "\" 库存不足");
            }
            // 扣减库存并更新销量
            product.setStock(product.getStock() - cartItem.getQuantity());
            product.setSales(product.getSales() + cartItem.getQuantity());
            productService.updateById(product);

            // 创建订单项
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice()); // 使用购物车中的价格快照
            orderItemMapper.insert(orderItem);

            // 组装返回给前端的 DTO
            OrderResponse.OrderItemInfo itemInfo = new OrderResponse.OrderItemInfo();
            BeanUtils.copyProperties(cartItem, itemInfo);
            itemInfo.setProductName(cartItem.getName());
            itemInfo.setProductImageUrl(cartItem.getImageUrl());
            orderItemInfos.add(itemInfo);
        }

        // 4. 清空购物车
        List<String> cartItemIds = cartItems.stream().map(CartItemDetailVO::getCartItemId).collect(Collectors.toList());
        cartItemService.deleteItemsFromCart(userId, cartItemIds);

        // 5. 组装并返回 OrderResponse
        OrderResponse response = new OrderResponse();
        BeanUtils.copyProperties(order, response);
        response.setItems(orderItemInfos);
        return response;
    }

    @Override
    public List<OrderResponse> getUserOrders(Long userId) {
        // 1. 根据用户ID查询所有订单
        QueryWrapper<Order> orderQuery = new QueryWrapper<>();
        orderQuery.eq("user_id", userId).orderByDesc("created_at");
        List<Order> orders = this.baseMapper.selectList(orderQuery);

        // 2. 遍历订单，查询每个订单的订单项
        return orders.stream().map(order -> {
            OrderResponse response = new OrderResponse();
            BeanUtils.copyProperties(order, response);

            QueryWrapper<OrderItem> itemQuery = new QueryWrapper<>();
            itemQuery.eq("order_id", order.getId());
            List<OrderItem> orderItems = orderItemMapper.selectList(itemQuery);

            // 3. 查询商品信息并组装 DTO
            List<OrderResponse.OrderItemInfo> itemInfos = orderItems.stream().map(item -> {
                Product product = productService.getById(item.getProductId());
                OrderResponse.OrderItemInfo itemInfo = new OrderResponse.OrderItemInfo();
                itemInfo.setProductId(item.getProductId());
                itemInfo.setProductName(product != null ? product.getName() : "商品已下架");
                itemInfo.setProductImageUrl(product != null ? product.getMainImageUrl() : "");
                itemInfo.setQuantity(item.getQuantity());
                itemInfo.setPrice(item.getPrice());
                return itemInfo;
            }).collect(Collectors.toList());

            response.setItems(itemInfos);
            return response;
        }).collect(Collectors.toList());
    }
}