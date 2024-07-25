package com.likelion.MoodMate.controller;

import com.likelion.MoodMate.dto.signUpDTO;
import com.likelion.MoodMate.entity.User;
import com.likelion.MoodMate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<Void> signUp(@RequestBody signUpDTO userDTO) {
        User user = new User();
        user.setUserId(userDTO.getUserId());
        user.setUserPassword(userDTO.getUserPw());
        user.setUserName(userDTO.getUserName());
        try {
            userService.signUp(user);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).build();
        }
    }
}
