package com.example.onlinemall.service.impl;

import com.clickhouse.jdbc.ClickHouseDataSource; // This import is no longer needed
import com.example.onlinemall.entity.UserBehavior;
import com.example.onlinemall.mapper.UserBehaviorMapper;
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
import java.util.List;

/**
 * User behavior service implementation - (writes to ClickHouse)
 */
@Service
public class UserBehaviorServiceImpl implements UserBehaviorService {

    private static final Logger logger = LoggerFactory.getLogger(UserBehaviorServiceImpl.class);

    private final DataSource dataSource;
    private final UserBehaviorMapper userBehaviorMapper; // 注入Mapper

    @Autowired
    public UserBehaviorServiceImpl(@Qualifier("clickhouseDataSource") DataSource dataSource, UserBehaviorMapper userBehaviorMapper) { // <--- 3. Change the constructor parameter type
        this.dataSource = dataSource;
        this.userBehaviorMapper = userBehaviorMapper;
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

    /**
     * 实现获取带有限制的用户行为的逻辑
     * @param limit 获取的记录条数
     * @return 用户行为列表
     */
    @Override
    public List<UserBehavior> getAllBehaviors(int limit) {
        return userBehaviorMapper.findAllOrderByTimestampDesc(limit);
    }
}