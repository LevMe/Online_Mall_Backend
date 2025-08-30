package com.example.onlinemall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.onlinemall.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户数据访问层接口
 * 继承 BaseMapper<User> 后，就获得了对 User 表的 CRUD 能力
 */
@Mapper // 标记这个接口是一个MyBatis的Mapper，并让Spring Boot能够扫描到它
public interface UserMapper extends BaseMapper<User> {
    // 目前我们不需要在这里定义任何方法
    // Mybatis-Plus提供的通用方法已经足够强大
    // 如果未来有复杂的多表查询，可以在这里自定义方法和对应的SQL
}
