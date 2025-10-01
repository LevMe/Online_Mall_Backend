package com.example.onlinemall.controller;

import com.example.onlinemall.common.Result;
import com.example.onlinemall.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/recommendations")
public class AdminRecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @PostMapping("/trigger-training")
    public Result<String> triggerTraining() {
        // 为了不阻塞接口，可以采用异步执行
        new Thread(() -> recommendationService.triggerModelTraining()).start();
        return Result.success("模型已开始执行离线训练 (异步模式)");
    }
}