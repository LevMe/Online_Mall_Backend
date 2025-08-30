package com.example.onlinemall.dto;

import lombok.Data;

/**
 * 添加商品到购物车的请求数据传输对象 (DTO)
 */
@Data
public class AddItemToCartRequest {
    private Long productId;
    private Integer quantity;
}