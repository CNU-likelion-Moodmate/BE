package com.likelion.MoodMate.controller;

import com.likelion.MoodMate.dto.DeleteQuestRequest;
import com.likelion.MoodMate.dto.CompleteQuestRequest;
import com.likelion.MoodMate.entity.QuestRecord;
import com.likelion.MoodMate.service.QuestRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class QuestRecordController {

    private final QuestRecordService questRecordService;

    @Autowired
    public QuestRecordController(QuestRecordService questRecordService) {
        this.questRecordService = questRecordService;
    }

    @PostMapping
    public ResponseEntity<QuestRecord> createQuestRecord(@RequestBody QuestRecord questRecord) {
        QuestRecord savedQuestRecord = questRecordService.createQuestRecord(questRecord);
        return ResponseEntity.ok(savedQuestRecord);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestRecord> getQuestRecord(@PathVariable Long id) {
        Optional<QuestRecord> questRecord = questRecordService.getQuestRecord(id);
        return questRecord.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestRecord> updateQuestRecord(@PathVariable Long id, @RequestBody QuestRecord questRecordDetails) {
        Optional<QuestRecord> updatedQuestRecord = questRecordService.updateQuestRecord(id, questRecordDetails);
        return updatedQuestRecord.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestRecord(@PathVariable Long id) {
        boolean isDeleted = questRecordService.deleteQuestRecord(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/loadQuest")
    public ResponseEntity<List<QuestRecord>> loadQuest(@RequestParam String userId) {
        List<QuestRecord> questRecords = questRecordService.findByUserId(userId);
        if (questRecords.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(questRecords);
    }


    @DeleteMapping("/deleteQuest")
    public ResponseEntity<Void> deleteQuest(@RequestBody DeleteQuestRequest request) {
        boolean isDeleted = questRecordService.deleteQuestRecord(request.getUserId(), request.getContents(), request.getDate());
        if (isDeleted) {

    @PostMapping("/completeQuest")
    public ResponseEntity<Void> completeQuest(@RequestBody CompleteQuestRequest request) {
        boolean isUpdated = questRecordService.completeQuest(request.getUserId(), request.getContents(), request.getDate(), request.getRating());
        if (isUpdated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
