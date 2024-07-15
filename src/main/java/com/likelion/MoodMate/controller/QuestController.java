package com.likelion.MoodMate.controller;

import com.likelion.MoodMate.entity.Quest;
import com.likelion.MoodMate.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/quests")
public class QuestController {

    private final QuestService questService;

    @Autowired
    public QuestController(QuestService questService) {
        this.questService = questService;
    }

    @PostMapping
    public ResponseEntity<Quest> createQuest(@RequestBody Quest quest) {
        Quest savedQuest = questService.createQuest(quest);
        return ResponseEntity.ok(savedQuest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quest> getQuest(@PathVariable Long id) {
        Optional<Quest> quest = questService.getQuest(id);
        return quest.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Quest> updateQuest(@PathVariable Long id, @RequestBody Quest questDetails) {
        Optional<Quest> updatedQuest = questService.updateQuest(id, questDetails);
        return updatedQuest.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuest(@PathVariable Long id) {
        boolean isDeleted = questService.deleteQuest(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
