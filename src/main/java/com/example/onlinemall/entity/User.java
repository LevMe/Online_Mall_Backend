package com.example.onlinemall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类，映射数据库中的 'users' 表
 */
@Data
@TableName("users")
public class User {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String email;

    private String avatarUrl;

    /**
     * 用户角色 (USER, ADMIN)
     */
    private String role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
