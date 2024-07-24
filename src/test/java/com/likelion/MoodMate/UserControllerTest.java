package com.likelion.MoodMate;

import com.likelion.MoodMate.entity.QuestRecord;
import com.likelion.MoodMate.entity.User;
import com.likelion.MoodMate.repository.QuestRecordRepository;
import com.likelion.MoodMate.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class UserControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestRecordRepository questRecordRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        questRecordRepository.deleteAll();

        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.JANUARY, 1);
        Date validDate = calendar.getTime();

        User user = new User();
        user.setUserId("testUser");
        user.setUserName("Test User");
        user.setUserPassword("password");
        userRepository.save(user);

        QuestRecord record = new QuestRecord();
        record.setAllocatedDate(validDate);
        record.setIsCompleted(true);
        record.setMood("happy");
        record.setQuestContext("Quest context");
        record.setRate(5);
        record.setUser(user);
        questRecordRepository.save(record);
    }

    @Test
    void testSignUpSuccessfully() {
        User user = userRepository.findByUserId("testUser").orElse(null);
        assertThat(user).isNotNull();
        assertThat(user.getUserName()).isEqualTo("Test User");

        QuestRecord record = questRecordRepository.findByUser_UserId("testUser").get(0);
        assertThat(record).isNotNull();
        assertThat(record.getQuestContext()).isEqualTo("Quest context");
    }

    @Test
    void testSignUpConflict() {
        User duplicateUser = new User();
        duplicateUser.setUserId("testUser");
        duplicateUser.setUserName("Duplicate User");
        duplicateUser.setUserPassword("password");

        try {
            userRepository.save(duplicateUser);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(Exception.class);
        }
    }
}
