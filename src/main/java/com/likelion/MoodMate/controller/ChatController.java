package com.likelion.MoodMate.controller;

import com.likelion.MoodMate.dto.ChatRequest;
import com.likelion.MoodMate.dto.ChatResponse;
import com.likelion.MoodMate.dto.ModelSelectionRequest;
import com.likelion.MoodMate.dto.ModelSelectionResponse;
import com.likelion.MoodMate.service.ChatService;
import com.likelion.MoodMate.service.FineTuneMessageGenerator;
import com.likelion.MoodMate.dto.ConversationHistory;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@SessionAttributes({"fineTuneMessage", "conversationHistory"})
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
    public ModelSelectionResponse selectModel(@RequestBody ModelSelectionRequest request, Model model, HttpSession session) throws JSONException {
        ModelSelectionResponse response = chatService.selectModel(request);
        String fineTuneMessage = fineTuneMessageGenerator.generateMessage(request.getSelectedModel());
        session.setAttribute("fineTuneMessage", fineTuneMessage);
        session.setAttribute("conversationHistory", new ConversationHistory(fineTuneMessage));
        logger.info("Model selected: {}, session updated with fineTuneMessage and conversationHistory.", request.getSelectedModel());
        return response;
    }

    @PostMapping("/chat")
    public ChatResponse sendMessage(@RequestBody ChatRequest request, @SessionAttribute("fineTuneMessage") String fineTuneMessage, @SessionAttribute("conversationHistory") ConversationHistory conversationHistory, HttpSession session) throws JSONException {
        ChatResponse response = chatService.sendMessage(request, fineTuneMessage, conversationHistory);
        session.setAttribute("conversationHistory", conversationHistory);
        logger.info("Session fineTuneMessage: {}, conversationHistory: {}", fineTuneMessage, conversationHistory.getHistory());
        return response;
    }
}
