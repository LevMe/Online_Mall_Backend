package com.example.onlinemall.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 管理员更新商品的请求数据传输对象 (DTO)
 */
@Data
public class UpdateProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Integer categoryId;
    private String mainImageUrl;
    private List<String> imageUrls;
    private Map<String, Object> specs;
}
