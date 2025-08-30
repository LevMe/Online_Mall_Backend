package com.example.onlinemall.dto;

import lombok.Data;

/**
 * 更新购物车商品数量的请求 DTO
 */
@Data
public class UpdateCartItemRequest {
    private Integer quantity;
}