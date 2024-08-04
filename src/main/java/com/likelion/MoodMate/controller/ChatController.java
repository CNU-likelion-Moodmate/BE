package com.likelion.MoodMate.controller;

import com.likelion.MoodMate.dto.ChatRequest;
import com.likelion.MoodMate.dto.ChatResponse;
import com.likelion.MoodMate.dto.ModelSelectionRequest;
import com.likelion.MoodMate.dto.ModelSelectionResponse;
import com.likelion.MoodMate.service.ChatService;
import com.likelion.MoodMate.service.FineTuneMessageGenerator;
import com.likelion.MoodMate.dto.ConversationHistory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private final ChatService chatService;
    private final FineTuneMessageGenerator fineTuneMessageGenerator;

    @Autowired
    public ChatController(ChatService chatService, FineTuneMessageGenerator fineTuneMessageGenerator) {
        this.chatService = chatService;
        this.fineTuneMessageGenerator = fineTuneMessageGenerator;
    }

    @PostMapping("/select-model")
    public ModelSelectionResponse selectModel(@RequestBody ModelSelectionRequest request, HttpServletRequest httpServletRequest) throws JSONException {
        HttpSession session = httpServletRequest.getSession();
        String selectedModel = request.getSelectedModel();
        logger.info("Requested model selection: {}", selectedModel);

        session.invalidate();
        session = httpServletRequest.getSession(true);
        logger.info("Session invalidated and new session created.");

        String fineTuneMessage = fineTuneMessageGenerator.generateMessage(selectedModel);
        logger.info("Generated fineTuneMessage: {}", fineTuneMessage);

        ConversationHistory newConversationHistory = new ConversationHistory(fineTuneMessage);
        logger.info("Initialized new ConversationHistory.");

        session.setAttribute("fineTuneMessage", fineTuneMessage);
        session.setAttribute("conversationHistory", newConversationHistory);
        session.setAttribute("selectedModel", selectedModel);
        logger.info("Session attributes set: fineTuneMessage, conversationHistory, selectedModel.");

        ModelSelectionResponse response = chatService.selectModel(request);
        logger.info("Model selected: {}, session updated.", selectedModel);

        return response;
    }

    @PostMapping("/chat")
    public ChatResponse sendMessage(
            @RequestBody ChatRequest request,
            HttpSession session) throws JSONException {

        String fineTuneMessage = (String) session.getAttribute("fineTuneMessage");
        ConversationHistory conversationHistory = (ConversationHistory) session.getAttribute("conversationHistory");

        if (fineTuneMessage == null || conversationHistory == null) {
            fineTuneMessage = fineTuneMessageGenerator.generateMessage("friendly");
            session.setAttribute("fineTuneMessage", fineTuneMessage);
            conversationHistory = new ConversationHistory(fineTuneMessage);
            session.setAttribute("conversationHistory", conversationHistory);
        }

        ChatResponse response = chatService.sendMessage(request, fineTuneMessage, conversationHistory);
        session.setAttribute("conversationHistory", conversationHistory);

        logger.info("Session fineTuneMessage: {}, conversationHistory: {}", fineTuneMessage, conversationHistory.getHistory());
        return response;
    }
}
