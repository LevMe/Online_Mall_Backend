package com.example.onlinemall.mapper;

import com.example.onlinemall.entity.UserBehavior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource; // <--- 1. 修改导入
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserBehaviorMapper {

    private final DataSource clickHouseDataSource; // <--- 2. 将类型改为通用的 DataSource

    @Autowired
    public UserBehaviorMapper(@Qualifier("clickhouseDataSource") DataSource clickHouseDataSource) { // <--- 3. 在构造函数中也修改类型
        this.clickHouseDataSource = clickHouseDataSource;
    }

    /**
     * 查询所有用户行为，并按时间戳降序排序
     * @return 用户行为列表
     */
    public List<UserBehavior> findAllOrderByTimestampDesc(int limit) {
        List<UserBehavior> behaviors = new ArrayList<>();
        // SQL查询语句，按时间倒序排列并限制数量
        String sql = "SELECT user_id, product_id, event_type, timestamp FROM user_behaviors ORDER BY timestamp DESC LIMIT ?";
        try (Connection conn = clickHouseDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, limit); // 设置LIMIT参数

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                UserBehavior behavior = new UserBehavior();
                behavior.setUserId(rs.getLong("user_id"));
                behavior.setProductId(rs.getLong("product_id"));
                behavior.setEventType(rs.getString("event_type"));
                behavior.setTimestamp(rs.getTimestamp("timestamp"));
                behaviors.add(behavior);
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询ClickHouse所有用户行为时出错", e);
        }
        return behaviors;
    }

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
        String sql = "SELECT product_id FROM user_behaviors WHERE user_id = ? AND event_type = 'click' ORDER BY timestamp DESC LIMIT ?";
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