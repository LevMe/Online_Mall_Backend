package com.example.onlinemall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.onlinemall.entity.UserBehavior;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户行为数据访问层接口
 */
@Mapper
public interface UserBehaviorMapper extends BaseMapper<UserBehavior> {
}