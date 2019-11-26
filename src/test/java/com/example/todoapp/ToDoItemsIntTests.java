package com.example.todoapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.todoapp.config.security.UserRole;
import com.example.todoapp.entity.ToDoItem;
import com.example.todoapp.entity.User;
import com.example.todoapp.repository.ToDoRepository;
import com.example.todoapp.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.servlet.http.Cookie;
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
class ToDoItemsIntTests {

    private static final String TODO_ITEM_TEXT = "todo_item_text";
    private static final String USERNAME = "username";
    private static final char[] PASSWORD = "password".toCharArray();
    private static final String SESSION_ID_COOKIE = "JSESSIONID";
    private String username;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ToDoRepository toDoRepository;

    @BeforeAll
    void before() throws Exception {
        username = getRandomUsername();
        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(new User(username, PASSWORD, UserRole.ROLE_USER))))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

//    @Test
    void addAndListItemsFailureUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/items")
            .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    void addAndListItemsSuccess() throws Exception {
        String session = login();
        mockMvc.perform(MockMvcRequestBuilders.get("/items")
            .cookie(new Cookie(SESSION_ID_COOKIE, session))
            .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(new ArrayList<ToDoItem>())));

        toDoRepository.save(new ToDoItem(1L, TODO_ITEM_TEXT, true, username));
        mockMvc.perform(MockMvcRequestBuilders.get("/items")
            .cookie(new Cookie(SESSION_ID_COOKIE, session))
            .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(new ArrayList<ToDoItem>())));
    }

    private String getRandomUsername(){
        return String.format("%s%8d",USERNAME, new Random().nextInt());
    }

    private String login() throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/login")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(new User(username, PASSWORD, UserRole.ROLE_USER))))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn().getResponse().getCookie(SESSION_ID_COOKIE).getValue();
    }
}
