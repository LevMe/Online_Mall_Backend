package com.example.onlinemall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.onlinemall.entity.UserBehavior;

import java.time.LocalDateTime;

/**
 * 用户行为服务接口
 */
public interface UserBehaviorService extends IService<UserBehavior> {

    /**
     * 记录一条用户行为
     * @param userId 用户ID
     * @param productId 商品ID
     * @param eventType 事件类型
     * @param timestamp 事件时间戳
     */
    void trackBehavior(Long userId, Long productId, String eventType, LocalDateTime timestamp);
}