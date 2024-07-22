package com.likelion.MoodMate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.MoodMate.entity.User;
import com.likelion.MoodMate.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("로그인 컨트롤러 (LoginController) 은(는)")
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        User user = new User();
        user.setUserId("test01");
        user.setUserPassword("test01");
        user.setUserName("Test User");
        userRepository.save(user);
    }

    @Nested
    class 로그인_시 {
        @Test
        @DisplayName("성공하면 200 상태코드와 함께 isMember 값을 true로 반환한다")
        void 로그인_성공() throws Exception {
            String json = objectMapper.writeValueAsString(Map.of("userId", "test01", "userPw", "test01"));

            mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.isMember").value(true));
        }

        @Test
        @DisplayName("존재하지 않는 아이디로 로그인 시도하면 401 상태코드를 반환한다")
        void 존재하지_않는_아이디() throws Exception {
            String json = objectMapper.writeValueAsString(Map.of("userId", "wrongId", "userPw", "test01"));

            mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("잘못된 비밀번호로 로그인 시도하면 401 상태코드를 반환한다")
        void 잘못된_비밀번호() throws Exception {
            String json = objectMapper.writeValueAsString(Map.of("userId", "test01", "userPw", "wrongPassword"));

            mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isUnauthorized());
        }
    }
}