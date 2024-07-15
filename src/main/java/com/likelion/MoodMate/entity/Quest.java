package com.likelion.MoodMate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "quests")
@Getter
@Setter
public class Quest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String mood;

    @Column(name = "quest_context", nullable = false)
    private String questContext;

    @Column(nullable = false)
    private Integer activity;
}