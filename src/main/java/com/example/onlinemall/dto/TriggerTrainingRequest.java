package com.example.onlinemall.dto;

import lombok.Data;

import java.util.List;

@Data
public class TriggerTrainingRequest {
    private List<UserBehaviorDTO> userBehaviors;
}