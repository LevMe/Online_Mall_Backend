package com.example.onlinemall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户行为实体类，映射 'user_behaviors' 表
 */
@Data
@TableName("user_behaviors")
public class UserBehavior {

    /**
     * 行为ID, 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 事件类型 (e.g., 'click', 'addToCart', 'purchase')
     */
    private String eventType;

    /**
     * 事件发生的时间戳
     */
    private LocalDateTime timestamp;
}
