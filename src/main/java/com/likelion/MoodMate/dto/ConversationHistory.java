package com.likelion.MoodMate.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ConversationHistory {
    private final List<String> history;
    private String fineTuneMessage;

    public ConversationHistory(String fineTuneMessage) {
        this.history = new ArrayList<>();
        this.fineTuneMessage = fineTuneMessage;
        this.history.add(fineTuneMessage); // 파인 튜닝 메시지를 그대로 추가
    }

    public void appendUserMessage(String message) {
        history.add("User: " + message);
    }

    public void appendAiMessage(String message) {
        history.add("AI: " + message);
    }

    public String getHistory() {
        return String.join("\n", history);
    }
}
