package com.example.todoapp.entity;

import com.example.todoapp.config.security.UserRole;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private char[] password;
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public User(String username, char[] password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
