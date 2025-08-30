package com.example.onlinemall.controller;

import com.example.onlinemall.common.Result;
import com.example.onlinemall.dto.TrackBehaviorRequest;
import com.example.onlinemall.interceptor.JwtInterceptor;
import com.example.onlinemall.service.UserBehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户行为控制器
 */
@RestController
@RequestMapping("/behaviors")
public class BehaviorController {

    private final UserBehaviorService userBehaviorService;

    @Autowired
    public BehaviorController(UserBehaviorService userBehaviorService) {
        this.userBehaviorService = userBehaviorService;
    }

    /**
     * 上报用户行为
     * @param request 包含行为数据的请求体
     * @return 操作结果
     */
    @PostMapping("/track")
    public Result<?> trackBehavior(@RequestBody TrackBehaviorRequest request) {
        Long userId = JwtInterceptor.getCurrentUserId();
        userBehaviorService.trackBehavior(
                userId,
                request.getProductId(),
                request.getEventType(),
                request.getTimestamp()
        );

        // 根据PRD，返回 202 Accepted
        return Result.accepted("行为已记录");
    }
}