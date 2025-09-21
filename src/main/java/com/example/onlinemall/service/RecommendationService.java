package com.example.onlinemall.service;

import com.example.onlinemall.dto.RecommendationRequest;
import com.example.onlinemall.dto.RecommendationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class RecommendationService {

    private final RestTemplate restTemplate;

    // 从 application.yml 中读取推荐服务的URL
    @Value("${recommendation.service.url}")
    private String recommendationServiceUrl;

    @Autowired
    public RecommendationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 调用外部推荐算法服务获取推荐结果
     *
     * @param userId       用户ID
     * @param recentClicks 用户最近点击的商品ID列表
     * @param topK         需要推荐的商品数量
     * @return 推荐商品ID列表
     */
    public List<String> getRecommendations(String userId, List<String> recentClicks, int topK) {
        RecommendationRequest request = new RecommendationRequest(userId, recentClicks, topK);
        try {
            RecommendationResponse response = restTemplate.postForObject(recommendationServiceUrl, request, RecommendationResponse.class);
            if (response != null) {
                return response.getRecommendedItemIds();
            }
        } catch (Exception e) {
            // 记录日志，处理异常，例如服务不可用
            // 在实际项目中，这里应该有更完善的日志和熔断降级策略
            System.err.println("Error calling recommendation service: " + e.getMessage());
        }
        return List.of(); // 返回空列表表示获取失败
    }
}