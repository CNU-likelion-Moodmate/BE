package com.likelion.MoodMate.service;

import com.likelion.MoodMate.dto.ChatRequest;
import com.likelion.MoodMate.dto.ChatResponse;
import com.likelion.MoodMate.dto.ModelSelectionRequest;
import com.likelion.MoodMate.dto.ModelSelectionResponse;
import com.likelion.MoodMate.dto.ConversationHistory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final OpenAiService openAiService;
    private final FineTuneMessageGenerator fineTuneMessageGenerator;

    @Autowired
    public ChatService(OpenAiService openAiService, FineTuneMessageGenerator fineTuneMessageGenerator) {
        this.openAiService = openAiService;
        this.fineTuneMessageGenerator = fineTuneMessageGenerator;
    }

    public ModelSelectionResponse selectModel(ModelSelectionRequest request) throws JSONException {
        String selectedModel = request.getSelectedModel();
        String fineTuneMessage = fineTuneMessageGenerator.generateMessage(selectedModel);
        String initialResponse = openAiService.getOpenAiResponse(fineTuneMessage);
        String responseMessage = extractMessageFromResponse(initialResponse);

        ModelSelectionResponse response = new ModelSelectionResponse();
        response.setResponseChat(responseMessage);
        return response;
    }

    public ChatResponse sendMessage(ChatRequest request, String fineTuneMessage, ConversationHistory conversationHistory) throws JSONException {
        String userMessage = request.getUserInput();
        conversationHistory.appendUserMessage(userMessage);

        String prompt = fineTuneMessage + "\n" + conversationHistory.getHistory();
        String openAiResponse = openAiService.getOpenAiResponse(prompt);
        String responseMessage = extractMessageFromResponse(openAiResponse);

        conversationHistory.appendAiMessage(responseMessage);

        return new ChatResponse(responseMessage);
    }

    private String extractMessageFromResponse(String jsonResponse) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        return jsonObject.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content");
    }
}
