package com.likelion.MoodMate;

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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {

    @Mock
    private OpenAiService openAiService;

    @Mock
    private FineTuneMessageGenerator fineTuneMessageGenerator;

    @InjectMocks
    private ChatService chatService;

    @BeforeEach
    void setUp() {
        chatService = new ChatService(openAiService, fineTuneMessageGenerator);
    }

    @Test
    void testSelectModel() throws JSONException {
        String model = "friendly";
        String fineTuneMessage = "Fine-tuned message for friendly model";
        String openAiResponse = "{\n" +
                "  \"id\": \"chatcmpl-9pTajXSuAgrIsrWs0eAS7lewRYV7Z\",\n" +
                "  \"object\": \"chat.completion\",\n" +
                "  \"created\": 1722055505,\n" +
                "  \"model\": \"gpt-4o-2024-05-13\",\n" +
                "  \"choices\": [\n" +
                "    {\n" +
                "      \"index\": 0,\n" +
                "      \"message\": {\n" +
                "        \"role\": \"assistant\",\n" +
                "        \"content\": \"안녕! 난 곰곰이라고 해! 너의 따뜻한 친구가 되고 싶어. 나에게 고민을 말해줄래?\"\n" +
                "      },\n" +
                "      \"logprobs\": null,\n" +
                "      \"finish_reason\": \"stop\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"usage\": {\n" +
                "    \"prompt_tokens\": 217,\n" +
                "    \"completion_tokens\": 31,\n" +
                "    \"total_tokens\": 248\n" +
                "  },\n" +
                "  \"system_fingerprint\": \"fp_bc2a86f5f5\"\n" +
                "}";

        when(fineTuneMessageGenerator.generateMessage(model)).thenReturn(fineTuneMessage);
        when(openAiService.getOpenAiResponse(fineTuneMessage)).thenReturn(openAiResponse);

        ModelSelectionRequest request = new ModelSelectionRequest();
        request.setSelectedModel(model);

        ModelSelectionResponse response = chatService.selectModel(request);

        assertNotNull(response);
        assertEquals("안녕! 난 곰곰이라고 해! 너의 따뜻한 친구가 되고 싶어. 나에게 고민을 말해줄래?", response.getResponseChat());
        verify(fineTuneMessageGenerator).generateMessage(model);
        verify(openAiService).getOpenAiResponse(fineTuneMessage);

        // Print statements to verify the intermediate results
        System.out.println("Fine-tuned message: " + fineTuneMessage);
        System.out.println("Initial response from OpenAiService: " + openAiResponse);
        System.out.println("Parsed response message: " + response.getResponseChat());
    }

    @Test
    void testSendMessage() throws JSONException {
        String model = "friendly";
        String fineTuneMessage = "Fine-tuned message for friendly model";
        String userMessage = "Hello!";
        String openAiResponse = "{\n" +
                "  \"id\": \"chatcmpl-9pTajXSuAgrIsrWs0eAS7lewRYV7Z\",\n" +
                "  \"object\": \"chat.completion\",\n" +
                "  \"created\": 1722055505,\n" +
                "  \"model\": \"gpt-4o-2024-05-13\",\n" +
                "  \"choices\": [\n" +
                "    {\n" +
                "      \"index\": 0,\n" +
                "      \"message\": {\n" +
                "        \"role\": \"assistant\",\n" +
                "        \"content\": \"안녕! 난 곰곰이라고 해! 너의 따뜻한 친구가 되고 싶어. 나에게 고민을 말해줄래?\"\n" +
                "      },\n" +
                "      \"logprobs\": null,\n" +
                "      \"finish_reason\": \"stop\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"usage\": {\n" +
                "    \"prompt_tokens\": 217,\n" +
                "    \"completion_tokens\": 31,\n" +
                "    \"total_tokens\": 248\n" +
                "  },\n" +
                "  \"system_fingerprint\": \"fp_bc2a86f5f5\"\n" +
                "}";

        when(fineTuneMessageGenerator.generateMessage(model)).thenReturn(fineTuneMessage);
        when(openAiService.getOpenAiResponse(anyString())).thenReturn(openAiResponse);

        // Select the model first
        ModelSelectionRequest modelRequest = new ModelSelectionRequest();
        modelRequest.setSelectedModel(model);
        chatService.selectModel(modelRequest);

        // Send the chat message
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setUserInput(userMessage);

        ChatResponse chatResponse = chatService.sendMessage(chatRequest);

        assertNotNull(chatResponse);
        assertEquals("안녕! 난 곰곰이라고 해! 너의 따뜻한 친구가 되고 싶어. 나에게 고민을 말해줄래?", chatResponse.getResponseChat());
        verify(openAiService, times(2)).getOpenAiResponse(anyString());

        // Print statements to verify the intermediate results
        System.out.println("User message: " + userMessage);
        System.out.println("Prompt sent to OpenAiService: " + fineTuneMessage + "\n" + userMessage);
        System.out.println("Response from OpenAiService: " + openAiResponse);
        System.out.println("Parsed response message: " + chatResponse.getResponseChat());
    }

    @Test
    void testSendMessageWithoutModelSelection() {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setUserInput("Hello!");

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            chatService.sendMessage(chatRequest);
        });

        assertEquals("Model not selected", exception.getMessage());
    }
}
