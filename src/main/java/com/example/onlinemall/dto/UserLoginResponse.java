package com.example.onlinemall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录成功响应的数据传输对象 (DTO)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponse {

    /**
     * 认证Token
     */
    private String token;

    /**
     * 用户基本信息
     */
    private UserInfo userInfo;

    /**
     * 内部类，用于封装用户信息
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private String userId;
        private String username;
        private String avatarUrl;
    }
}
