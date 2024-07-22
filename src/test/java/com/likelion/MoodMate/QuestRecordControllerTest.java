package com.likelion.MoodMate;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class QuestRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestRecordService questRecordService;

    private User testUser;
    private QuestRecord questRecord1;
    private QuestRecord questRecord2;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUserId("test01");
        testUser.setUserPassword("password");
        testUser.setUserName("Test User");

        questRecord1 = new QuestRecord();
        questRecord1.setId(1L);
        questRecord1.setUser(testUser);
        questRecord1.setIsCompleted(true);
        questRecord1.setAllocatedDate(new Date());
        questRecord1.setQuestContext("퀘스트 내용 1");
        questRecord1.setMood("happy");
        questRecord1.setRate(5);

        questRecord2 = new QuestRecord();
        questRecord2.setId(2L);
        questRecord2.setUser(testUser);
        questRecord2.setIsCompleted(false);
        questRecord2.setAllocatedDate(new Date());
        questRecord2.setQuestContext("퀘스트 내용 2");
        questRecord2.setMood("sad");
        questRecord2.setRate(3);
    }

    @Test
    public void testLoadQuest() throws Exception {
        Mockito.when(questRecordService.findByUserId("test01")).thenReturn(Arrays.asList(questRecord1, questRecord2));

        // 날짜 형식을 동일하게 맞추기 위한 SimpleDateFormat 사용
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String dateStr1 = dateFormat.format(questRecord1.getAllocatedDate());
        String dateStr2 = dateFormat.format(questRecord2.getAllocatedDate());

        String expectedJson = String.format("[{\"id\":1,\"user\":{\"id\":1,\"userId\":\"test01\",\"userPassword\":\"password\",\"userName\":\"Test User\"},\"isCompleted\":true,\"allocatedDate\":\"%s\",\"questContext\":\"퀘스트 내용 1\",\"mood\":\"happy\",\"rate\":5},{\"id\":2,\"user\":{\"id\":1,\"userId\":\"test01\",\"userPassword\":\"password\",\"userName\":\"Test User\"},\"isCompleted\":false,\"allocatedDate\":\"%s\",\"questContext\":\"퀘스트 내용 2\",\"mood\":\"sad\",\"rate\":3}]",
                dateStr1, dateStr2);

        mockMvc.perform(get("/questRecords/loadQuest")
                        .param("userId", "test01")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }
}
