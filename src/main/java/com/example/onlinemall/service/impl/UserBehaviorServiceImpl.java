package com.example.onlinemall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.onlinemall.entity.UserBehavior;
import com.example.onlinemall.mapper.UserBehaviorMapper;
import com.example.onlinemall.service.UserBehaviorService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户行为服务实现类
 */
@Service
public class UserBehaviorServiceImpl extends ServiceImpl<UserBehaviorMapper, UserBehavior> implements UserBehaviorService {

    @Override
    public void trackBehavior(Long userId, Long productId, String eventType, LocalDateTime timestamp) {
        UserBehavior behavior = new UserBehavior();
        behavior.setUserId(userId);
        behavior.setProductId(productId);
        behavior.setEventType(eventType);
        behavior.setTimestamp(timestamp);

        // 将行为数据插入数据库
        this.baseMapper.insert(behavior);
    }

    @Override
    public List<UserBehavior> getAllBehaviors() {
        return baseMapper.selectAll();
    }
}