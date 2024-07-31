package com.likelion.MoodMate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestRecordResponse {
    private Long questRecordId;
    private String contents;
    private String date;
    private int rate;

    public QuestRecordResponse(Long questRecordId, String contents, String date, int rate) {
        this.questRecordId = questRecordId;
        this.contents = contents;
        this.date = date;
        this.rate = rate;
    }
}
