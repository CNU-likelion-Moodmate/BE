package com.likelion.MoodMate.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SaveQuestRequest {
    private List<String> selectedQuests;
    private String userId;
}
