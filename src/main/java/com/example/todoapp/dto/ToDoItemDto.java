package com.example.todoapp.dto;

import lombok.Data;

@Data
public class ToDoItemDto {
    private Long id;
    private String text;
    private boolean checked;

}
