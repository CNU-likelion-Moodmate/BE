package com.likelion.MoodMate.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

@Service
public class OpenAiService {

    private final RestTemplate restTemplate;

    @Value("${spring.ai.openai.base-url}")
    private String apiUrl;
    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    public OpenAiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getOpenAiResponse(String prompt) {
        String url = apiUrl;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o");
        requestBody.put("messages", new Object[] { new HashMap<String, String>() {{
            put("role", "system");
            put("content", prompt);
        }}});
        requestBody.put("max_tokens", 512);
        requestBody.put("temperature", 0.2);

        try {
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to get response from OpenAI: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            return e.getResponseBodyAsString();
        }
    }
}
