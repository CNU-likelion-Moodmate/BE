package com.likelion.MoodMate.repository;

import com.likelion.MoodMate.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestRepository extends JpaRepository<Quest, Long> {
    Optional<Quest> findByQuestContext(String questContext);
    List<Quest> findByMoodAndActivityBetween(String mood, Integer minActivity, Integer maxActivity);

}
