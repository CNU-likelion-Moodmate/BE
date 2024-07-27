package com.likelion.MoodMate.service;

import com.likelion.MoodMate.entity.QuestRecord;
import com.likelion.MoodMate.repository.QuestRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class QuestRecordService {

    private final QuestRecordRepository questRecordRepository;

    @Autowired
    public QuestRecordService(QuestRecordRepository questRecordRepository) {
        this.questRecordRepository = questRecordRepository;
    }

    public boolean deleteQuestRecord(String userId, String contents, Date date) {
        Optional<QuestRecord> questRecord = questRecordRepository.findByUser_UserIdAndQuestContextAndAllocatedDate(userId, contents, date);
        if (questRecord.isPresent()) {
            questRecordRepository.delete(questRecord.get());
            return true;
        } else {
            return false;
        }
    }

    public List<QuestRecord> findByUserId(String userId) {
        return questRecordRepository.findByUser_UserId(userId);
    }

    public boolean completeQuest(String userId, String contents, Date date, int rating) {
        Optional<QuestRecord> optionalQuestRecord = questRecordRepository.findByUser_UserIdAndQuestContextAndAllocatedDate(userId, contents, date);
        if (optionalQuestRecord.isPresent()) {
            QuestRecord questRecord = optionalQuestRecord.get();
            questRecord.setIsCompleted(true);
            questRecord.setRate(rating);
            questRecordRepository.save(questRecord);
            return true;
        } else {
            return false;
        }
    }
}
