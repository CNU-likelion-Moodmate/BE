package com.likelion.MoodMate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CorsTestController {

    @GetMapping("/test")
    public String testCors() {
        return "CORS is working!";
    }
}
