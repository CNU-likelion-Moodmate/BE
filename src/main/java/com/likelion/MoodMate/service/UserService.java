package com.likelion.MoodMate.service;

import com.likelion.MoodMate.entity.User;
import com.likelion.MoodMate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> validateUser(String userId, String userPw) {
        return userRepository.findByUserIdAndUserPassword(userId, userPw);
    }

    public boolean isUserIdExists(String userId) {
        return userRepository.findByUserId(userId).isPresent();
    }

    public void signUp(User user) throws IllegalArgumentException {
        if (isUserIdExists(user.getUserId())) {
            throw new IllegalArgumentException("User ID already exists");
        }
        userRepository.save(user);
    }
}