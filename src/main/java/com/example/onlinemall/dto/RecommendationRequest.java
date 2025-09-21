package com.example.onlinemall.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationRequest {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("recent_clicks")
    private List<String> recentClicks;

    @JsonProperty("top_k")
    private int topK;
}