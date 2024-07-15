package com.likelion.MoodMate.service;

import com.likelion.MoodMate.entity.Quest;
import com.likelion.MoodMate.repository.QuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestService {

    private final QuestRepository questRepository;

    @Autowired
    public QuestService(QuestRepository questRepository) {
        this.questRepository = questRepository;
    }

    public Quest createQuest(Quest quest) {
        return questRepository.save(quest);
    }

    public Optional<Quest> getQuest(Long id) {
        return questRepository.findById(id);
    }

    public Optional<Quest> updateQuest(Long id, Quest questDetails) {
        Optional<Quest> optionalQuest = questRepository.findById(id);
        if (optionalQuest.isPresent()) {
            Quest quest = optionalQuest.get();
            quest.setMood(questDetails.getMood());
            quest.setQuestContext(questDetails.getQuestContext());
            quest.setActivity(questDetails.getActivity());
            return Optional.of(questRepository.save(quest));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteQuest(Long id) {
        if (questRepository.existsById(id)) {
            questRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
