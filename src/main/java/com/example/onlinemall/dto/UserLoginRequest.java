package com.example.onlinemall.dto;

import lombok.Data;

/**
 * 用户登录请求的数据传输对象 (DTO)
 */
@Data
public class UserLoginRequest {
    private String username;
    private String password;
}
