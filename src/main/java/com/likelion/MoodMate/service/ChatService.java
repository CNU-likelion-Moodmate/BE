package com.likelion.MoodMate.service;

import com.likelion.MoodMate.dto.ChatRequest;
import com.likelion.MoodMate.dto.ChatResponse;
import com.likelion.MoodMate.dto.ModelSelectionRequest;
import com.likelion.MoodMate.dto.ModelSelectionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final OpenAiService openAiService;
    private final FineTuneMessageGenerator fineTuneMessageGenerator;

    private String currentModel;
    private String currentFineTuneMessage;

    @Autowired
    public ChatService(OpenAiService openAiService, FineTuneMessageGenerator fineTuneMessageGenerator) {
        this.openAiService = openAiService;
        this.fineTuneMessageGenerator = fineTuneMessageGenerator;
    }

    public ModelSelectionResponse selectModel(ModelSelectionRequest request) {
        this.currentModel = request.getModel();
        this.currentFineTuneMessage = fineTuneMessageGenerator.generateMessage(request.getModel());
        String initialResponse = openAiService.getOpenAiResponse(this.currentFineTuneMessage);

        ModelSelectionResponse response = new ModelSelectionResponse();
        response.setMessage(initialResponse);
        return response;
    }

    public ChatResponse sendMessage(ChatRequest request) {
        if (currentModel == null || currentFineTuneMessage == null) {
            throw new IllegalStateException("Model not selected");
        }

        String userMessage = request.getMessage();
        String prompt = this.currentFineTuneMessage + "\n" + userMessage;
        String openAiResponse = openAiService.getOpenAiResponse(prompt);

        ChatResponse response = new ChatResponse();
        response.setResponse(openAiResponse);
        return response;
    }
}
