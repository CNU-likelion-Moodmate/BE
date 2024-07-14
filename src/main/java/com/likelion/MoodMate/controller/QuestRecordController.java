package com.likelion.MoodMate.controller;

import com.likelion.MoodMate.entity.QuestRecord;
import com.likelion.MoodMate.service.QuestRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/questRecords")
public class QuestRecordController {

    @Autowired
    private QuestRecordService questRecordService;

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
}
