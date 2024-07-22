package com.likelion.MoodMate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompleteQuestRequest {
    private String userId;
    private String contents;
    private String date;
    private int rating;
}
