package com.example.onlinemall.service;

import com.example.onlinemall.dto.RecommendationRequest;
import com.example.onlinemall.dto.RecommendationResponse;
import com.example.onlinemall.dto.TriggerTrainingRequest;
import com.example.onlinemall.dto.UserBehaviorDTO;
import com.example.onlinemall.entity.UserBehavior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final RestTemplate restTemplate;

    @Autowired
    private UserBehaviorService userBehaviorService;

    // 从 application.yml 中读取推荐服务的URL
    @Value("${recommendation.service.url}")
    private String recommendationServiceUrl;

    @Value("${recommendation.trigger-training-endpoint.url}")
    private String triggerTrainingEndpoint;

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

    /**
     * 新增方法：触发推荐模型训练
     */
    public void triggerModelTraining() {
        // 1. 从数据库获取所有用户行为数据
        List<UserBehavior> behaviors = userBehaviorService.getAllBehaviors();
        if (behaviors == null || behaviors.isEmpty()) {
            System.out.println("No user behaviors found in the database. Skipping training trigger.");
            return;
        }

        // 2. 将实体列表转换为 DTO 列表
        // 注意：UserBehavior 实体中的 timestamp 是 LocalDateTime，需要转换为 Unix 时间戳 (Long)
        List<UserBehaviorDTO> behaviorDTOs = behaviors.stream().map(behavior -> {
            UserBehaviorDTO dto = new UserBehaviorDTO();
            dto.setUserId(Math.toIntExact(behavior.getUserId()));
            dto.setProductId(Math.toIntExact(behavior.getProductId()));
            if (behavior.getTimestamp() != null) {
                dto.setTimestamp(behavior.getTimestamp().toEpochSecond(ZoneOffset.UTC));
            }
            return dto;
        }).collect(Collectors.toList());

        // 3. 构造请求体
        TriggerTrainingRequest trainingRequest = new TriggerTrainingRequest();
        trainingRequest.setUserBehaviors(behaviorDTOs);

        // 4. 发送 POST 请求到算法服务
        String url = triggerTrainingEndpoint;
        try {
            System.out.println("Triggering recommendation model training...");
            // 使用 postForObject，可以接收返回结果
            String response = restTemplate.postForObject(url, trainingRequest, String.class);
            System.out.println("Trigger training response from recommendation service: " + response);
        } catch (Exception e) {
            System.err.println("Failed to trigger recommendation model training. Error: " + e.getMessage());
            // 在实际项目中，这里应该使用日志框架记录错误
        }
    }
}