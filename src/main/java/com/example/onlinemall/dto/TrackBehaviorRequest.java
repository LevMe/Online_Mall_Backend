package com.example.onlinemall.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 上报用户行为的请求 DTO
 */
@Data
public class TrackBehaviorRequest {
    private String eventType; // 'click', 'addToCart', 'purchase'
    private Long productId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private LocalDateTime timestamp;
}