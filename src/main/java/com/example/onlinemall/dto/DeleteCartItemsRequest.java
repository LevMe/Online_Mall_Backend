package com.example.onlinemall.dto;

import lombok.Data;
import java.util.List;

/**
 * 从购物车删除商品的请求 DTO
 */
@Data
public class DeleteCartItemsRequest {
    private List<String> cartItemIds;
}