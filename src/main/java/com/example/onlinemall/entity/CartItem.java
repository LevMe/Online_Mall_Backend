package com.example.onlinemall.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 购物车项实体类，映射 'cart_items' 表
 */
@Data
@TableName("cart_items")
public class CartItem {

    /**
     * 购物车项ID, 主键, 使用UUID
     */
    @TableId(value = "id")
    private String id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品数量
     */
    private Integer quantity;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
