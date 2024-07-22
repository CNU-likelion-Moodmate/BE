package com.likelion.MoodMate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveQuestRequest {
    private Long questId;    // 선택한 퀘스트의 ID
    private String userId;
}
