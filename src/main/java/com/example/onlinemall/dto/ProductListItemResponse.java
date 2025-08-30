package com.example.onlinemall.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品列表项的响应数据传输对象 (DTO)
 */
@Data
public class ProductListItemResponse {
    private Long id;
    private String name;
    private int stock;
    private BigDecimal price;
    private String mainImageUrl;
}