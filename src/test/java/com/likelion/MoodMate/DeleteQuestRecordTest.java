package com.likelion.MoodMate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.MoodMate.dto.DeleteQuestRequest;
import com.likelion.MoodMate.entity.QuestRecord;
import com.likelion.MoodMate.entity.User;
import com.likelion.MoodMate.repository.QuestRecordRepository;
import com.likelion.MoodMate.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DeleteQuestRecordTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QuestRecordRepository questRecordRepository;

    @Autowired
    private UserRepository userRepository;

    private DeleteQuestRequest deleteQuestRequest;
    private Date questDate;
    private User testUser;
    private QuestRecord testQuestRecord;

    @BeforeEach
    @Transactional
    @Rollback(false)
    void setUp() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        questDate = dateFormat.parse("2024-07-21");

        // 사용자 초기화
        testUser = userRepository.findByUserId("test01");
        if (testUser == null) {
            testUser = new User();
            testUser.setUserId("test01");
            testUser.setUserPassword("password");
            testUser.setUserName("Test User");
            userRepository.save(testUser);
        }

        // 퀘스트 레코드 초기화
        testQuestRecord = new QuestRecord();
        testQuestRecord.setUser(testUser);
        testQuestRecord.setQuestContext("퀘스트 내용");
        testQuestRecord.setAllocatedDate(questDate);
        testQuestRecord.setIsCompleted(false);
        testQuestRecord.setMood("happy");
        testQuestRecord.setRate(5);
        questRecordRepository.save(testQuestRecord);

        deleteQuestRequest = new DeleteQuestRequest();
        deleteQuestRequest.setUserId("test01");
        deleteQuestRequest.setContents("퀘스트 내용");
        deleteQuestRequest.setDate(questDate);
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteQuestSuccessfully() throws Exception {
        mockMvc.perform(delete("/deleteQuest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(deleteQuestRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteQuestNotFound() throws Exception {
        deleteQuestRequest.setContents("없는 퀘스트 내용");

        mockMvc.perform(delete("/deleteQuest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(deleteQuestRequest)))
                .andExpect(status().isNotFound());
    }
}
