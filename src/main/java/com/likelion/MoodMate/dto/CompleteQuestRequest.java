package com.likelion.MoodMate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompleteQuestRequest {
    private Long questRecordId;
    private int rate;
}
