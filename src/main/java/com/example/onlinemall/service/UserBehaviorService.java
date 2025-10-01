package com.example.onlinemall.service;

import com.example.onlinemall.entity.UserBehavior;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户行为服务接口 - (已改造为写入ClickHouse)
 */
public interface UserBehaviorService {

    /**
     * 记录一条用户行为到ClickHouse
     * @param userId 用户ID
     * @param productId 商品ID
     * @param eventType 事件类型
     * @param timestamp 事件时间戳
     */
    void trackBehavior(Long userId, Long productId, String eventType, LocalDateTime timestamp);

    /**
     * 获取用户行为记录，支持限制数量
     * @param limit 获取的记录条数
     * @return 用户行为列表
     */
    List<UserBehavior> getAllBehaviors(int limit);

}