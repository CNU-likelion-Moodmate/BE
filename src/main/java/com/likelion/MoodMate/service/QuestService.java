package com.likelion.MoodMate.service;

import com.likelion.MoodMate.entity.Quest;
import com.likelion.MoodMate.entity.QuestRecord;
import com.likelion.MoodMate.entity.User;
import com.likelion.MoodMate.repository.QuestRecordRepository;
import com.likelion.MoodMate.repository.QuestRepository;
import com.likelion.MoodMate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class QuestService {

    private final QuestRepository questRepository;
    private final UserRepository userRepository;
    private final QuestRecordRepository questRecordRepository;

    @Autowired
    public QuestService(QuestRepository questRepository, UserRepository userRepository, QuestRecordRepository questRecordRepository) {
        this.questRepository = questRepository;
        this.userRepository = userRepository;
        this.questRecordRepository = questRecordRepository;
    }

    public void saveQuest(String selectedQuest, String userId) {
        User user = userRepository.findByUserId(userId).orElse(null);
        Quest quest = questRepository.findByQuestContext(selectedQuest)
                .orElseThrow(() -> new RuntimeException("Quest not found"));

        if (user != null) {
            QuestRecord questRecord = new QuestRecord();
            questRecord.setUser(user);
            questRecord.setQuestContext(quest.getQuestContext());
            questRecord.setMood(quest.getMood());
            questRecord.setAllocatedDate(new Date());
            questRecord.setIsCompleted(false);
            questRecord.setRate(0); // 기본값
            questRecordRepository.save(questRecord);
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
