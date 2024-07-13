package com.likelion.MoodMate;

import com.likelion.MoodMate.entity.User;
import com.likelion.MoodMate.repository.UserRepository;
import com.likelion.MoodMate.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MoodMateApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/users";
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setUserId("testuser");
        user.setUserPassword("password123");
        user.setUserName("Test User");

        ResponseEntity<User> responseEntity = restTemplate.postForEntity(baseUrl, user, User.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        User createdUser = responseEntity.getBody();
        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());

        // 데이터베이스에 유저가 있는지 확인
        Optional<User> savedUser = userRepository.findById(createdUser.getId());
        assertTrue(savedUser.isPresent());
        assertEquals("testuser", savedUser.get().getUserId());
        assertEquals("Test User", savedUser.get().getUserName());
        assertEquals("password123", savedUser.get().getUserPassword());

        // Clean up
        userRepository.deleteById(createdUser.getId());
    }

    @Test
    public void testGetUser() {
        User user = new User();
        user.setUserId("testuser");
        user.setUserPassword("password123");
        user.setUserName("Test User");
        User savedUser = userRepository.save(user);

        ResponseEntity<User> responseEntity = restTemplate.getForEntity(baseUrl + "/" + savedUser.getId(), User.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        User fetchedUser = responseEntity.getBody();
        assertNotNull(fetchedUser);
        assertEquals(savedUser.getUserId(), fetchedUser.getUserId());

        // Clean up
        userRepository.deleteById(savedUser.getId());
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setUserId("testuser");
        user.setUserPassword("password123");
        user.setUserName("Test User");
        User savedUser = userRepository.save(user);

        savedUser.setUserName("Updated User");
        HttpEntity<User> requestEntity = new HttpEntity<>(savedUser);
        ResponseEntity<User> responseEntity = restTemplate.exchange(baseUrl + "/" + savedUser.getId(), HttpMethod.PUT, requestEntity, User.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        User updatedUser = responseEntity.getBody();
        assertNotNull(updatedUser);
        assertEquals("Updated User", updatedUser.getUserName());

        // Clean up
        userRepository.deleteById(savedUser.getId());
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setUserId("testuser");
        user.setUserPassword("password123");
        user.setUserName("Test User");
        User savedUser = userRepository.save(user);

        restTemplate.delete(baseUrl + "/" + savedUser.getId());
        Optional<User> deletedUser = userRepository.findById(savedUser.getId());
        assertFalse(deletedUser.isPresent());
    }
}
