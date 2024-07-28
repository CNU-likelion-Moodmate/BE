package com.likelion.MoodMate;

import com.likelion.MoodMate.config.RestTemplateConfig;
import com.likelion.MoodMate.dto.ChatRequest;
import com.likelion.MoodMate.dto.ChatResponse;
import com.likelion.MoodMate.dto.ModelSelectionRequest;
import com.likelion.MoodMate.dto.ModelSelectionResponse;
import com.likelion.MoodMate.service.ChatService;
import com.likelion.MoodMate.service.FineTuneMessageGenerator;
import com.likelion.MoodMate.service.OpenAiService;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import java.util.Objects;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(RestTemplateConfig.class)
class ChatControllerTest {

    @Autowired
    private ChatService chatService;

    @Autowired
    private FineTuneMessageGenerator fineTuneMessageGenerator;

    @Autowired
    private OpenAiService openAiService;

    private Model model;

    private static final Logger logger = Logger.getLogger(ChatControllerTest.class.getName());

    @BeforeEach
    void setUp() {
        model = new BindingAwareModelMap();
    }

    @Test
    void testSelectModel() throws JSONException {
        ModelSelectionRequest request = new ModelSelectionRequest();
        request.setSelectedModel("friendly");

        ModelSelectionResponse response = logModelSelection(request);

        assertNotNull(response);
        assertNotNull(response.getResponseChat());
        System.out.println("Model Selection Response: " + response.getResponseChat());

        model.addAttribute("fineTuneMessage", request.getSelectedModel());
        model.addAttribute("conversationHistory", new StringBuilder());
    }

    @Test
    void testSendMessageFriendly() throws JSONException {
        ModelSelectionRequest modelRequest = new ModelSelectionRequest();
        modelRequest.setSelectedModel("friendly");
        ModelSelectionResponse modelResponse = logModelSelection(modelRequest);

        assertNotNull(modelResponse);
        model.addAttribute("fineTuneMessage", modelRequest.getSelectedModel());
        model.addAttribute("conversationHistory", new StringBuilder());

        ChatRequest firstChatRequest = new ChatRequest();
        firstChatRequest.setUserInput("나 요즘 우울해서 힘이 안나..");
        ChatResponse firstChatResponse = logChatMessage(firstChatRequest, (String) model.getAttribute("fineTuneMessage"), (StringBuilder) Objects.requireNonNull(model.getAttribute("conversationHistory")));

        assertNotNull(firstChatResponse);
        System.out.println("First Chat Response (Friendly): " + firstChatResponse.getResponseChat());

        ChatRequest secondChatRequest = new ChatRequest();
        secondChatRequest.setUserInput("하는 일도 잘 안돼구.. 답답하고 힘들어");
        ChatResponse secondChatResponse = logChatMessage(secondChatRequest, (String) model.getAttribute("fineTuneMessage"), (StringBuilder) Objects.requireNonNull(model.getAttribute("conversationHistory")));

        assertNotNull(secondChatResponse);
        System.out.println("Second Chat Response (Friendly): " + secondChatResponse.getResponseChat());
    }

    @Test
    void testSendMessageSerious() throws JSONException {
        ModelSelectionRequest modelRequest = new ModelSelectionRequest();
        modelRequest.setSelectedModel("serious");
        ModelSelectionResponse modelResponse = logModelSelection(modelRequest);

        assertNotNull(modelResponse);
        model.addAttribute("fineTuneMessage", modelRequest.getSelectedModel());
        model.addAttribute("conversationHistory", new StringBuilder());

        ChatRequest firstChatRequest = new ChatRequest();
        firstChatRequest.setUserInput("오해가 생겨 친구와 크게 싸우고 말았어요..");
        ChatResponse firstChatResponse = logChatMessage(firstChatRequest, (String) model.getAttribute("fineTuneMessage"), (StringBuilder) model.getAttribute("conversationHistory"));

        assertNotNull(firstChatResponse);
        System.out.println("First Chat Response (Serious): " + firstChatResponse.getResponseChat());

        ChatRequest secondChatRequest = new ChatRequest();
        secondChatRequest.setUserInput("친구가 저에 대해 험담을 했다고 들어서 크게 화를 냈는데 사실 그게 아니었어요..");
        ChatResponse secondChatResponse = logChatMessage(secondChatRequest, (String) model.getAttribute("fineTuneMessage"), (StringBuilder) model.getAttribute("conversationHistory"));

        assertNotNull(secondChatResponse);
        System.out.println("Second Chat Response (Serious): " + secondChatResponse.getResponseChat());
    }

    private ModelSelectionResponse logModelSelection(ModelSelectionRequest request) throws JSONException {
        ModelSelectionResponse response = chatService.selectModel(request);
        logger.info("Model Selection Request: " + request);
        logger.info("Model Selection Response: " + response);
        return response;
    }

    private ChatResponse logChatMessage(ChatRequest request, String fineTuneMessage, StringBuilder conversationHistory) throws JSONException {
        ChatResponse response = chatService.sendMessage(request, fineTuneMessage, conversationHistory);
        logger.info("Chat Request: " + request);
        logger.info("Chat Prompt: " + fineTuneMessage + conversationHistory.toString());
        logger.info("Chat Response: " + response);
        return response;
    }
}
