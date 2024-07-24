package com.likelion.MoodMate;

import com.likelion.MoodMate.entity.Quest;
import com.likelion.MoodMate.entity.QuestRecord;
import com.likelion.MoodMate.entity.User;
import com.likelion.MoodMate.repository.QuestRecordRepository;
import com.likelion.MoodMate.repository.QuestRepository;
import com.likelion.MoodMate.repository.UserRepository;
import com.likelion.MoodMate.service.QuestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class QuestServiceTest {

    @Mock
    private QuestRepository questRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private QuestRecordRepository questRecordRepository;

    @InjectMocks
    private QuestService questService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveQuest_Success() {
        User user = new User();
        user.setUserId("test01");

        Quest quest = new Quest();
        quest.setQuestContext("퀘스트 내용");
        quest.setMood("기분");

        when(userRepository.findByUserId("test01")).thenReturn(user);
        when(questRepository.findByQuestContext("퀘스트 내용")).thenReturn(Optional.of(quest));

        questService.saveQuest("퀘스트 내용", "test01");

        verify(questRecordRepository, times(1)).save(any(QuestRecord.class));
    }

    @Test
    public void testSaveQuest_QuestNotFound() {
        User user = new User();
        user.setUserId("test01");

        when(userRepository.findByUserId("test01")).thenReturn(user);
        when(questRepository.findByQuestContext("퀘스트 내용")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            questService.saveQuest("퀘스트 내용", "test01");
        });
    }

    @Test
    public void testSaveQuest_UserNotFound() {
        when(userRepository.findByUserId("test01")).thenReturn(null);

        assertThrows(RuntimeException.class, () -> {
            questService.saveQuest("퀘스트 내용", "test01");
        });
    }
}
