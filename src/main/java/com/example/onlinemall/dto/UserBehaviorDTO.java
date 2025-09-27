package com.example.onlinemall.dto;

import lombok.Data;

@Data
public class UserBehaviorDTO {
    private Integer userId;
    private Integer productId;
    private Long timestamp; // 使用 Long 类型来表示 Unix 时间戳
}