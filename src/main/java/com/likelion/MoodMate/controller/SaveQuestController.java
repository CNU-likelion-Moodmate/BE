package com.likelion.MoodMate.controller;

import com.likelion.MoodMate.dto.SaveQuestRequest;
import com.likelion.MoodMate.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/saveQuest")
public class SaveQuestController {

    private final QuestService questService;

    @Autowired
    public SaveQuestController(QuestService questService) {
        this.questService = questService;
    }

    @PostMapping
    public ResponseEntity<Void> saveQuest(@RequestBody SaveQuestRequest saveQuestRequest) {
        try {
            questService.saveQuest(saveQuestRequest.getSelectedQuest(), saveQuestRequest.getUserId());
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
