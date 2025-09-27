package com.example.onlinemall.scheduler;

import com.example.onlinemall.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RecommendationTrainingScheduler {

    @Autowired
    private RecommendationService recommendationService;

    /**
     * 每天凌晨3点执行模型训练任务
     * cron表达式: 秒 分 时 日 月 周
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void scheduleTrainingTask() {
        System.out.println("Scheduled recommendation training task started at " + LocalDateTime.now());
        recommendationService.triggerModelTraining();
        System.out.println("Scheduled recommendation training task finished at " + LocalDateTime.now());
    }
}