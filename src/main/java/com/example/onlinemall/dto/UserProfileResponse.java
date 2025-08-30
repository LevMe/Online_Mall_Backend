package com.example.onlinemall.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 获取当前登录用户信息接口的响应数据传输对象 (DTO)
 */
@Data
public class UserProfileResponse {

    private String userId;
    private String username;
    private String email;
    private String avatarUrl;

    /**
     * @JsonFormat 用于指定日期时间字段在序列化为JSON时的格式
     * 这将覆盖我们在 application.yml 中设置的全局格式，以满足PRD中带'T'和'Z'的ISO 8601格式
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private LocalDateTime createdAt;
}
