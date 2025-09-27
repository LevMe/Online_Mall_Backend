package com.example.onlinemall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.onlinemall.entity.UserBehavior;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户行为数据访问层接口
 */
@Mapper
public interface UserBehaviorMapper extends BaseMapper<UserBehavior> {
    @Select("SELECT * FROM user_behaviors")
    List<UserBehavior> selectAll();
}