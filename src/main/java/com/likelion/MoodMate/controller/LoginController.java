package com.likelion.MoodMate.controller;

import com.likelion.MoodMate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Boolean>> loginUser(@RequestBody Map<String, String> loginData) {
        String userId = loginData.get("userId");
        String userPw = loginData.get("userPw");
        boolean isMember = userService.validateUser(userId, userPw);
        if (!isMember) {
            return ResponseEntity.status(401).body(Map.of("isMember", false));
        }

        return ResponseEntity.ok(Map.of("isMember", isMember));
    }
}
