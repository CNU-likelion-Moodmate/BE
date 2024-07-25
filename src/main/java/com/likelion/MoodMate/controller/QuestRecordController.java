package com.likelion.MoodMate.controller;

import com.likelion.MoodMate.dto.DeleteQuestRequest;
import com.likelion.MoodMate.dto.CompleteQuestRequest;
import com.likelion.MoodMate.entity.QuestRecord;
import com.likelion.MoodMate.service.QuestRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QuestRecordController {

    private final QuestRecordService questRecordService;

    @Autowired
    public QuestRecordController(QuestRecordService questRecordService) {
        this.questRecordService = questRecordService;
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
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

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

