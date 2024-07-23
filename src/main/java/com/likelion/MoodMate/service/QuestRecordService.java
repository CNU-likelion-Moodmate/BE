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

    public QuestRecord createQuestRecord(QuestRecord questRecord) {
        return questRecordRepository.save(questRecord);
    }

    public Optional<QuestRecord> getQuestRecord(Long id) {
        return questRecordRepository.findById(id);
    }

    public Optional<QuestRecord> updateQuestRecord(Long id, QuestRecord questRecordDetails) {
        Optional<QuestRecord> optionalQuestRecord = questRecordRepository.findById(id);
        if (optionalQuestRecord.isPresent()) {
            QuestRecord questRecord = optionalQuestRecord.get();
            questRecord.setIsCompleted(questRecordDetails.getIsCompleted());
            questRecord.setAllocatedDate(questRecordDetails.getAllocatedDate());
            questRecord.setQuestContext(questRecordDetails.getQuestContext());
            questRecord.setMood(questRecordDetails.getMood());
            questRecord.setRate(questRecordDetails.getRate());
            return Optional.of(questRecordRepository.save(questRecord));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteQuestRecord(Long id) {
        if (questRecordRepository.existsById(id)) {
            questRecordRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
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
}
