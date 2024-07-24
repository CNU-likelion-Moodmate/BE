package com.likelion.MoodMate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.MoodMate.dto.CompleteQuestRequest;
import com.likelion.MoodMate.entity.QuestRecord;
import com.likelion.MoodMate.entity.User;
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
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompleteQuestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestRecordService questRecordService;

    private User testUser;
    private QuestRecord questRecord;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUserId("test01");
        testUser.setUserPassword("password");
        testUser.setUserName("Test User");

        questRecord = new QuestRecord();
        questRecord.setId(1L);
        questRecord.setUser(testUser);
        questRecord.setIsCompleted(false);
        questRecord.setAllocatedDate(new Date());
        questRecord.setQuestContext("퀘스트 내용");
        questRecord.setMood("happy");
        questRecord.setRate(5);
    }

    @Test
    public void testCompleteQuestSuccessfully() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateFormat.format(new Date());

        CompleteQuestRequest request = new CompleteQuestRequest();
        request.setUserId("test01");
        request.setContents("퀘스트 내용");
        request.setDate(dateStr);
        request.setRating(4);

        Mockito.when(questRecordService.completeQuest("test01", "퀘스트 내용", dateStr, 4)).thenReturn(true);

        mockMvc.perform(post("/questRecords/completeQuest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void testCompleteQuestNotFound() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateFormat.format(new Date());

        CompleteQuestRequest request = new CompleteQuestRequest();
        request.setUserId("test01");
        request.setContents("퀘스트 내용");
        request.setDate(dateStr);
        request.setRating(4);

        Mockito.when(questRecordService.completeQuest("test01", "퀘스트 내용", dateStr, 4)).thenReturn(false);

        mockMvc.perform(post("/questRecords/completeQuest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
