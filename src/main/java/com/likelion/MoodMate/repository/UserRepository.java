package com.likelion.MoodMate.repository;

import com.likelion.MoodMate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserIdAndUserPassword(String userId, String userPassword);
    Optional<User> findByUserId(String userId);

}
