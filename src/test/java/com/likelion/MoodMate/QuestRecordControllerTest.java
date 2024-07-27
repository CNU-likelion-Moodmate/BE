package com.likelion.MoodMate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.MoodMate.dto.CompleteQuestRequest;
import com.likelion.MoodMate.dto.DeleteQuestRequest;
import com.likelion.MoodMate.entity.QuestRecord;
import com.likelion.MoodMate.service.QuestRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class QuestRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestRecordService questRecordService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    }

    @Test
    void testLoadQuest() throws Exception {
        String userId = "test01";
        QuestRecord questRecord = new QuestRecord();
        questRecord.setQuestContext("퀘스트 내용");
        questRecord.setAllocatedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2024-07-10"));

        List<QuestRecord> questRecords = Collections.singletonList(questRecord);

        Mockito.when(questRecordService.findByUserId(userId)).thenReturn(questRecords);

        mockMvc.perform(get("/loadQuest")
                        .param("userId", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].questContext").value("퀘스트 내용"))
                .andExpect(jsonPath("$[0].allocatedDate").value("2024-07-10"));
    }

    @Test
    void testDeleteQuest() throws Exception {
        DeleteQuestRequest request = new DeleteQuestRequest();
        request.setUserId("test01");
        request.setContents("퀘스트 내용");
        request.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2024-07-10"));

        Mockito.when(questRecordService.deleteQuestRecord(eq("test01"), eq("퀘스트 내용"), any(Date.class))).thenReturn(true);

        mockMvc.perform(delete("/deleteQuest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testCompleteQuest() throws Exception {
        CompleteQuestRequest request = new CompleteQuestRequest();
        request.setUserId("test01");
        request.setContents("퀘스트 내용");
        request.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2024-07-10"));
        request.setRating(4);

        // Mock 설정
        Mockito.when(questRecordService.completeQuest(eq("test01"), eq("퀘스트 내용"), any(Date.class), eq(4))).thenReturn(true);

        mockMvc.perform(post("/completeQuest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
