package com.likelion.MoodMate.controller;

import com.likelion.MoodMate.dto.RecommendQuestRequest;
import com.likelion.MoodMate.dto.RecommendQuestResponse;
import com.likelion.MoodMate.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecommendQuestController {

    private final QuestService questService;

    @Autowired
    public RecommendQuestController(QuestService questService) {
        this.questService = questService;
    }

    @PostMapping("/recommendQuest")
    public RecommendQuestResponse recommendQuest(@RequestBody RecommendQuestRequest request) {
        List<String> recommendedQuests = questService.recommendQuests(request.getMood1(), request.getMood2(), request.getActivity());
        return new RecommendQuestResponse(recommendedQuests);
    }
}
