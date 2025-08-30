package com.example.onlinemall.dto;

import lombok.Data;

/**
 * 用户注册请求的数据传输对象 (DTO)
 * 用于封装前端POST /users/register接口的请求体
 */
@Data
public class UserRegisterRequest {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 电子邮箱
     */
    private String email;
}
