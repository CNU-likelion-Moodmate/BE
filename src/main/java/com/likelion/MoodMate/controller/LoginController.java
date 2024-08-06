package com.likelion.MoodMate.controller;

import com.likelion.MoodMate.entity.User;
import com.likelion.MoodMate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Map<String, String> loginData) {
        String userId = loginData.get("userId");
        String userPw = loginData.get("userPw");
        Optional<User> userOptional = userService.validateUser(userId, userPw);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("isMember", false));
        }

        User user = userOptional.get();
        return ResponseEntity.ok(Map.of(
                "userId", user.getUserId(),
                "userName", user.getUserName()
        ));
    }
}
