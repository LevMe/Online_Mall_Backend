package com.example.onlinemall.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class RecommendationResponse {

    @JsonProperty("recommended_item_ids")
    private List<String> recommendedItemIds;
}