package com.example.onlinemall.entity;

import java.sql.Timestamp;

/**
 * 用户行为实体类 (对应ClickHouse中的表)
 */
public class UserBehavior {
    private Long userId;
    private Long productId;
    private String eventType;
    private Timestamp timestamp;

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp createdAt) {
        this.timestamp = createdAt;
    }
}