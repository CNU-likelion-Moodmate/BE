package com.likelion.MoodMate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.MoodMate.dto.SaveQuestRequest;
import com.likelion.MoodMate.service.QuestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SaveQuestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestService questService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveQuest_Success() throws Exception {
        SaveQuestRequest request = new SaveQuestRequest();
        request.setSelectedQuest("퀘스트 내용");
        request.setUserId("test01");

        mockMvc.perform(post("/saveQuest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(questService).saveQuest("퀘스트 내용", "test01");
    }

    @Test
    public void testSaveQuest_Failure() throws Exception {
        SaveQuestRequest request = new SaveQuestRequest();
        request.setSelectedQuest("퀘스트 내용");
        request.setUserId("test01");

        doThrow(new RuntimeException("퀘스트를 찾을 수 없습니다.")).when(questService).saveQuest("퀘스트 내용", "test01");

        mockMvc.perform(post("/saveQuest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
