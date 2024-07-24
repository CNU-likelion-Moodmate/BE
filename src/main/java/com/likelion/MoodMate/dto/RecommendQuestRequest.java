package com.likelion.MoodMate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendQuestRequest {
    private String mood1;
    private String mood2;
    private Integer activity;
}