package com.example.onlinemall.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RecommendationService {

    /**
     * 调用外部推荐算法服务获取推荐结果
     *
     * @param userId       用户ID
     * @param recentClicks 用户最近点击的商品ID列表
     * @param topK         需要推荐的商品数量
     * @return 推荐商品ID列表
     */
    List<String> getRecommendations(String userId, List<String> recentClicks, int topK);

    /**
     * 新增方法：触发推荐模型训练
     */
    void triggerModelTraining();
}