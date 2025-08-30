package com.example.onlinemall.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商品详情接口的响应数据传输对象 (DTO)
 */
@Data
public class ProductDetailResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private String description;
    private List<String> imageUrls;
    private Map<String, Object> specs;

}
