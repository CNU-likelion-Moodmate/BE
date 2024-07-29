package com.likelion.MoodMate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestRecordResponse {
    private String contents;
    private String date;

    public QuestRecordResponse(String contents, String date) {
        this.contents = contents;
        this.date = date;
    }
}
