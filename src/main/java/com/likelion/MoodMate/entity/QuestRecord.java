package com.likelion.MoodMate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "quest_records")
@Getter
@Setter
public class QuestRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Boolean isCompleted;

    @Column(nullable = false)
    private Date allocatedDate;

    @Column(nullable = false)
    private String questContext;

    @Column(nullable = false)
    private String mood;

    @Column(nullable = false)
    private Integer rate;
}
