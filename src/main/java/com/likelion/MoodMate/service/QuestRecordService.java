package com.likelion.MoodMate.service;

import com.likelion.MoodMate.entity.QuestRecord;
import com.likelion.MoodMate.repository.QuestRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public boolean deleteQuestRecord(String userId, String questContext, Date date) {
        Optional<QuestRecord> questRecord = questRecordRepository.findByUser_UserIdAndQuestContextAndAllocatedDate(userId, questContext, date);
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

    public boolean completeQuest(String userId, String contents, String dateStr, int rating) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

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
