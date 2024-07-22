package com.likelion.MoodMate;

import com.likelion.MoodMate.config.RestTemplateConfig;
import com.likelion.MoodMate.dto.ChatRequest;
import com.likelion.MoodMate.dto.ChatResponse;
import com.likelion.MoodMate.dto.ModelSelectionRequest;
import com.likelion.MoodMate.dto.ModelSelectionResponse;
import com.likelion.MoodMate.service.ChatService;
import com.likelion.MoodMate.service.FineTuneMessageGenerator;
import com.likelion.MoodMate.service.OpenAiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(RestTemplateConfig.class)
class ChatServiceIntegrationTest {

    @Autowired
    private ChatService chatService;

    @Autowired
    private FineTuneMessageGenerator fineTuneMessageGenerator;

    @Autowired
    private OpenAiService openAiService;

    @BeforeEach
    void setUp() {
        // 필요한 초기화 작업이 있으면 수행
    }

    @Test
    void testSelectModel() {
        ModelSelectionRequest request = new ModelSelectionRequest();
        request.setModel("friendly");

        ModelSelectionResponse response = chatService.selectModel(request);

        assertNotNull(response);
        System.out.println("Model Selection Response: " + response.getMessage());
    }

    @Test
    void testSendMessage() {
        // Friendly 모델 선택 및 첫 번째 메시지 보내기
        ModelSelectionRequest modelRequest = new ModelSelectionRequest();
        modelRequest.setModel("friendly");
        chatService.selectModel(modelRequest);

        ChatRequest firstChatRequest = new ChatRequest();
        firstChatRequest.setMessage("안녕");
        ChatResponse firstChatResponse = chatService.sendMessage(firstChatRequest);

        assertNotNull(firstChatResponse);
        System.out.println("First Chat Response (Friendly): " + firstChatResponse.getResponse());

        // 두 번째 메시지 보내기
        ChatRequest secondChatRequest = new ChatRequest();
        secondChatRequest.setMessage("나 요즘 우울해서 힘이 안나.. 어떡해야 할까?");
        ChatResponse secondChatResponse = chatService.sendMessage(secondChatRequest);

        assertNotNull(secondChatResponse);
        System.out.println("Second Chat Response (Friendly): " + secondChatResponse.getResponse());
    }

    @Test
    void testSendMessage2() {
        // Serious 모델 선택 및 첫 번째 메시지 보내기
        ModelSelectionRequest modelRequest = new ModelSelectionRequest();
        modelRequest.setModel("serious");
        chatService.selectModel(modelRequest);

        ChatRequest firstChatRequest = new ChatRequest();
        firstChatRequest.setMessage("안녕");
        ChatResponse firstChatResponse = chatService.sendMessage(firstChatRequest);

        assertNotNull(firstChatResponse);
        System.out.println("First Chat Response (Serious): " + firstChatResponse.getResponse());

        // 두 번째 메시지 보내기
        ChatRequest secondChatRequest = new ChatRequest();
        secondChatRequest.setMessage("회사에서 실수를 해서 부장님께 혼나고 말았어요.. 어떻게 해야 할까요?");
        ChatResponse secondChatResponse = chatService.sendMessage(secondChatRequest);

        assertNotNull(secondChatResponse);
        System.out.println("Second Chat Response (Serious): " + secondChatResponse.getResponse());
    }
}