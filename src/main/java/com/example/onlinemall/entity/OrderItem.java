package com.example.onlinemall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单项实体类
 */
@Data
@TableName("order_items")
public class OrderItem {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String orderId;

    private Long productId;

    private Integer quantity;

    private BigDecimal price;
}