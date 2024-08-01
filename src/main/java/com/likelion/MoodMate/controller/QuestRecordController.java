package com.likelion.MoodMate.controller;

import com.likelion.MoodMate.dto.DeleteQuestRequest;
import com.likelion.MoodMate.dto.CompleteQuestRequest;
import com.likelion.MoodMate.dto.QuestRecordResponse;
import com.likelion.MoodMate.entity.QuestRecord;
import com.likelion.MoodMate.service.QuestRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class QuestRecordController {

    private final QuestRecordService questRecordService;

    @Autowired
    public QuestRecordController(QuestRecordService questRecordService) {
        this.questRecordService = questRecordService;
    }

    @GetMapping("/loadQuest")
    public ResponseEntity<List<QuestRecordResponse>> loadQuest(@RequestParam String userId) {
        List<QuestRecord> questRecords = questRecordService.findByUserId(userId);
        if (questRecords.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<QuestRecordResponse> response = questRecords.stream()
                .map(record -> new QuestRecordResponse(
                        record.getId(),
                        record.getQuestContext(),
                        dateFormat.format(record.getAllocatedDate()),
                        record.getRate()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteQuest")
    public ResponseEntity<Void> deleteQuest(@RequestBody DeleteQuestRequest request) {
        boolean isDeleted = questRecordService.deleteQuestRecord(request.getQuestRecordId());
        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/completeQuest")
    public ResponseEntity<Void> completeQuest(@RequestBody CompleteQuestRequest request) {
        boolean isUpdated = questRecordService.completeQuest(request.getQuestRecordId(), request.getRate());
        if (isUpdated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
