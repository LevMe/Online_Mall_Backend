package com.example.onlinemall.service;

import java.time.LocalDateTime;

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

}