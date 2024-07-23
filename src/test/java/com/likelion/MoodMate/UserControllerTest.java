package com.likelion.MoodMate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.MoodMate.dto.signUpDTO;
import com.likelion.MoodMate.entity.User;
import com.likelion.MoodMate.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private signUpDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new signUpDTO();
        userDTO.setUserId("test01");
        userDTO.setUserPw("test01");
        userDTO.setUserName("testName");
    }

    @Test
    public void testSignUpSuccessfully() throws Exception {
        Mockito.when(userService.signUp(Mockito.any(User.class))).thenReturn(new User());

        mockMvc.perform(post("/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void testSignUpConflict() throws Exception {
        Mockito.doThrow(new IllegalArgumentException("User ID already exists")).when(userService).signUp(Mockito.any(User.class));

        mockMvc.perform(post("/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isConflict());
    }
}
