package com.likelion.MoodMate.service;

import com.likelion.MoodMate.dto.ChatRequest;
import com.likelion.MoodMate.dto.ChatResponse;
import com.likelion.MoodMate.dto.ModelSelectionRequest;
import com.likelion.MoodMate.dto.ModelSelectionResponse;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.json.JSONObject;

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

    public ModelSelectionResponse selectModel(ModelSelectionRequest request) throws JSONException {
        this.currentModel = request.getSelectedModel();
        this.currentFineTuneMessage = fineTuneMessageGenerator.generateMessage(request.getSelectedModel());
        String initialResponse = openAiService.getOpenAiResponse(this.currentFineTuneMessage);
        String responseMessage = extractMessageFromResponse(initialResponse);

        ModelSelectionResponse response = new ModelSelectionResponse();
        response.setResponseChat(responseMessage);
        return response;
    }

    public ChatResponse sendMessage(ChatRequest request) throws JSONException {
        if (currentModel == null || currentFineTuneMessage == null) {
            throw new IllegalStateException("Model not selected");
        }

        String userMessage = request.getUserInput();
        String prompt = this.currentFineTuneMessage + "\n" + userMessage;
        String openAiResponse = openAiService.getOpenAiResponse(prompt);
        String responseMessage = extractMessageFromResponse(openAiResponse);

        ChatResponse response = new ChatResponse();
        response.setResponseChat(responseMessage);
        return response;
    }

    private String extractMessageFromResponse(String jsonResponse) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        return jsonObject.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content");
    }
}
