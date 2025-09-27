package com.example.onlinemall.mapper;

import com.clickhouse.jdbc.ClickHouseDataSource;
import com.example.onlinemall.entity.UserBehavior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于操作ClickHouse中用户行为数据的Mapper
 */
@Repository
public class UserBehaviorMapper {

    @Autowired
    private ClickHouseDataSource clickHouseDataSource;

    /**
     * 根据用户ID和事件类型统计行为数量
     *
     * @param userId    用户ID
     * @return 行为数量
     */
    public Long selectCount(Long userId) {
        // SQL查询语句，注意表名应为 'user_behaviors'
        String sql = "SELECT count() FROM user_behaviors WHERE user_id = ?";
        try (Connection conn = clickHouseDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            // 在实际项目中，这里应该使用更完善的异常处理机制，例如日志记录
            throw new RuntimeException("查询ClickHouse用户行为计数时出错", e);
        }
        return 0L;
    }

    /**
     * 获取用户最近点击的商品列表
     *
     * @param userId 用户ID
     * @param limit  数量限制
     * @return 用户行为列表
     */
    public List<UserBehavior> findRecentClicksByUserId(Long userId, int limit) {
        List<UserBehavior> behaviors = new ArrayList<>();
        // SQL查询语句，按时间倒序排列
        String sql = "SELECT product_id FROM user_behaviors WHERE user_id = ? AND event_type = 'click' ORDER BY created_at DESC LIMIT ?";
        try (Connection conn = clickHouseDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            pstmt.setInt(2, limit);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                UserBehavior behavior = new UserBehavior();
                behavior.setProductId(rs.getLong("product_id"));
                behaviors.add(behavior);
            }
        } catch (SQLException e) {
            // 异常处理
            throw new RuntimeException("查询ClickHouse用户最近点击行为时出错", e);
        }
        return behaviors;
    }
}