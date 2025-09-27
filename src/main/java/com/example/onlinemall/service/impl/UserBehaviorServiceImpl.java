package com.example.onlinemall.service.impl;

import com.clickhouse.jdbc.ClickHouseDataSource;
import com.example.onlinemall.service.UserBehaviorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * 用户行为服务实现类 - (已改造为写入ClickHouse)
 */
@Service
public class UserBehaviorServiceImpl implements UserBehaviorService {

    private static final Logger logger = LoggerFactory.getLogger(UserBehaviorServiceImpl.class);

    private final ClickHouseDataSource dataSource;

    @Autowired
    public UserBehaviorServiceImpl(ClickHouseDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void trackBehavior(Long userId, Long productId, String eventType, LocalDateTime timestamp) {
        // ClickHouse 的写入操作最好是异步批量进行，但这里为了简单直接同步写入
        // 在生产环境中，建议使用消息队列（如Kafka）来缓冲数据，然后由一个专门的服务批量写入ClickHouse
        String sql = "INSERT INTO user_behaviors (user_id, product_id, event_type, timestamp) VALUES (?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            pstmt.setLong(2, productId);
            pstmt.setString(3, eventType);
            pstmt.setTimestamp(4, Timestamp.valueOf(timestamp));

            pstmt.executeUpdate();

        } catch (SQLException e) {
            // 记录异常，但不要抛出阻断主流程。行为上报失败不应影响用户体验。
            logger.error("Failed to track user behavior to ClickHouse", e);
        }
    }
}