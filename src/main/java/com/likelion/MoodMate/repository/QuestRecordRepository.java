package com.likelion.MoodMate.repository;

import com.likelion.MoodMate.entity.QuestRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestRecordRepository extends JpaRepository<QuestRecord, Long> {
    List<QuestRecord> findByUser_UserId(String userId);
}
