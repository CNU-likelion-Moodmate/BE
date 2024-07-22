package com.likelion.MoodMate.repository;

import com.likelion.MoodMate.entity.QuestRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface QuestRecordRepository extends JpaRepository<QuestRecord, Long> {
    List<QuestRecord> findByUser_UserId(String userId);
    Optional<QuestRecord> findByUser_UserIdAndQuestContextAndAllocatedDate(String userId, String questContext, Date allocatedDate);
}
