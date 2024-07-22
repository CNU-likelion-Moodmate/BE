package com.likelion.MoodMate.repository;

import com.likelion.MoodMate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserIdAndUserPassword(String userId, String userPassword);

}
