package com.likelion.MoodMate.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DeleteQuestRequest {
    private String userId;
    private String contents;
    private Date date;
}
