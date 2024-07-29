package com.likelion.MoodMate.repository;

import com.likelion.MoodMate.entity.QuestRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuestRecordRepository extends JpaRepository<QuestRecord, Long> {
    List<QuestRecord> findByUser_UserId(String userId);

    @Query("SELECT qr FROM QuestRecord qr JOIN qr.user u WHERE u.userId = :userId AND qr.questContext = :questContext AND FUNCTION('DATE_FORMAT', qr.allocatedDate, '%Y-%m-%d') = :date")
    Optional<QuestRecord> findByUser_UserIdAndQuestContextAndAllocatedDate(@Param("userId") String userId, @Param("questContext") String questContext, @Param("date") String date);
}
