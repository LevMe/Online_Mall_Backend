package com.example.onlinemall.service.impl;

import com.clickhouse.jdbc.ClickHouseDataSource; // This import is no longer needed
import com.example.onlinemall.service.UserBehaviorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier; // <-- Add this import if missing
import org.springframework.stereotype.Service;

import javax.sql.DataSource; // <--- 1. Add this import
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * User behavior service implementation - (writes to ClickHouse)
 */
@Service
public class UserBehaviorServiceImpl implements UserBehaviorService {

    private static final Logger logger = LoggerFactory.getLogger(UserBehaviorServiceImpl.class);

    private final DataSource dataSource; // <--- 2. Change the type here

    @Autowired
    public UserBehaviorServiceImpl(@Qualifier("clickhouseDataSource") DataSource dataSource) { // <--- 3. Change the constructor parameter type
        this.dataSource = dataSource;
    }

    @Override
    public void trackBehavior(Long userId, Long productId, String eventType, LocalDateTime timestamp) {
        String sql = "INSERT INTO user_behaviors (user_id, product_id, event_type, timestamp) VALUES (?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection(); // No other changes needed here
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            pstmt.setLong(2, productId);
            pstmt.setString(3, eventType);
            pstmt.setTimestamp(4, Timestamp.valueOf(timestamp));

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("Failed to track user behavior to ClickHouse", e);
        }
    }
}