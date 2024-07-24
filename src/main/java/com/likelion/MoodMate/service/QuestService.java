package com.likelion.MoodMate.service;

import com.likelion.MoodMate.entity.Quest;
import com.likelion.MoodMate.entity.QuestRecord;
import com.likelion.MoodMate.entity.User;
import com.likelion.MoodMate.repository.QuestRecordRepository;
import com.likelion.MoodMate.repository.QuestRepository;
import com.likelion.MoodMate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestService {

    private final QuestRepository questRepository;
    private final UserRepository userRepository;
    private final QuestRecordRepository questRecordRepository;

    @Autowired
    public QuestService(QuestRepository questRepository, UserRepository userRepository, QuestRecordRepository questRecordRepository) {
        this.questRepository = questRepository;
        this.userRepository = userRepository;
        this.questRecordRepository = questRecordRepository;
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

    public void saveQuest(String selectedQuest, String userId) {
        User user = userRepository.findByUserId(userId).orElse(null);
        Quest quest = questRepository.findByQuestContext(selectedQuest)
                .orElseThrow(() -> new RuntimeException("Quest not found"));

        if (user != null) {
            QuestRecord questRecord = new QuestRecord();
            questRecord.setUser(user);
            questRecord.setQuestContext(quest.getQuestContext());
            questRecord.setMood(quest.getMood());
            questRecord.setAllocatedDate(new Date());
            questRecord.setIsCompleted(false);
            questRecord.setRate(0); // 기본값
            questRecordRepository.save(questRecord);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public List<String> recommendQuests(String mood1, String mood2, Integer activity) {
        Random random = new Random();
        String selectedMood = random.nextBoolean() ? mood1 : mood2;
        List<Quest> quests = questRepository.findByMoodAndActivityGreaterThanEqual(selectedMood, activity);

        return quests.stream()
                .map(Quest::getQuestContext)
                .collect(Collectors.collectingAndThen(Collectors.toList(),
                        collected -> {
                            Collections.shuffle(collected);
                            return collected.stream().limit(3).collect(Collectors.toList());
                        }));
    }
}
