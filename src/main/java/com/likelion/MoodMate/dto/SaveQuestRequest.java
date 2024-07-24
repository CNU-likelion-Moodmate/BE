package com.likelion.MoodMate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveQuestRequest {
    private String selectedQuest;
    private String userId;
}
