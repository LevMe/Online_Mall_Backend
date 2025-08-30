package com.example.onlinemall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 购物车项详情的视图对象 (View Object)
 * 用于接收Mapper层JOIN查询返回的复合数据
 */
@Data
public class CartItemDetailVO {

    // 来自 cart_items 表
    private String cartItemId; // 我们将 cart_items.id 别名为 cartItemId
    private Long productId;
    private Integer quantity;

    // 来自 products 表
    private String name;
    private BigDecimal price;
    private String imageUrl; // 我们将 products.main_image_url 别名为 imageUrl
}