package com.likelion.MoodMate.repository;

import com.likelion.MoodMate.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestRepository extends JpaRepository<Quest, Long> {
}
