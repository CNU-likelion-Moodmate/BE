package com.likelion.MoodMate.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OpenAIResponse {

    private List<Choice> choices;

    @Getter
    @Setter
    public static class Choice {
        private String text;
        private int index;
        private String finish_reason;
    }
}
