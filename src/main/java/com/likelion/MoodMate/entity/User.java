package com.likelion.MoodMate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userid", nullable = false)
    private String userId;

    @Column(name = "userpassword", nullable = false)
    private String userPassword;

    @Column(name = "username", nullable = false)
    private String userName;
}
