package com.example.todoapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.todoapp.config.security.UserRole;
import com.example.todoapp.entity.User;
import com.example.todoapp.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
class UserIntTests {

    private static final String USERNAME = "username";
    private static final char[] PASSWORD = "password".toCharArray();
    private static final char[] WRONG_PASSWORD = "password2".toCharArray();
    private static char[] PASSWORD_ENCODED;

    private static final String SESSION_ID_COOKIE = "JSESSIONID";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeAll
    void before() {
        PASSWORD_ENCODED = passwordEncoder.encode(new String(PASSWORD)).toCharArray();
    }

    @Test
    void registrationSuccess() throws Exception {
        String username = getRandomUsername();
        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(new User(username, PASSWORD, UserRole.ROLE_USER))))
            .andExpect(MockMvcResultMatchers.status().isOk());

        List<User> users = userRepository.findByUsername(username);
        assertEquals(1, users.size());
        assertThat(users.get(0).getUsername()).isEqualTo(username);
        assertTrue(passwordEncoder.matches(new String(PASSWORD), new String(users.get(0).getPassword())));
    }

    @Test
    void registrationFailureWrongInput() throws Exception {
        String username = getRandomUsername();
        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(new User(username, PASSWORD, UserRole.ROLE_USER))))
            .andExpect(MockMvcResultMatchers.status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(new User(username, PASSWORD, UserRole.ROLE_USER))))
            .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.msg").exists());
    }

    @Test
    void loginSuccess() throws Exception {
        String username = getRandomUsername();
        userRepository.save(new User(username, PASSWORD_ENCODED, UserRole.ROLE_USER));
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(new User(username, PASSWORD, UserRole.ROLE_USER))))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.cookie().exists(SESSION_ID_COOKIE));
    }

    @Test
    void loginFailed() throws Exception {
        String username = getRandomUsername();
        userRepository.save(new User(username, PASSWORD_ENCODED, UserRole.ROLE_USER));
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(new User(username, WRONG_PASSWORD, UserRole.ROLE_USER))))
            .andExpect(MockMvcResultMatchers.status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    private String getRandomUsername(){
        return String.format("%s%8d",USERNAME, new Random().nextInt());
    }
}
