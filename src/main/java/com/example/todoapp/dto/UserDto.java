package com.example.todoapp.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    @NotNull
    private String username;
    @NotNull
    private char[] password;
    private String role;
}
