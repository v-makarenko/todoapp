package com.example.todoapp.controller;


import com.example.todoapp.dto.ToDoItemDto;
import com.example.todoapp.service.ToDoItemService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/items")
public class ToDoItemController {

    @Autowired
    private ToDoItemService toDoItemService;

    @GetMapping
    public ResponseEntity<List<ToDoItemDto>> list() {
        return ResponseEntity.ok(toDoItemService.list());
    }

    @PostMapping
    public ResponseEntity<ToDoItemDto> create(@RequestBody ToDoItemDto toDoItemDto) {
        return ResponseEntity.ok(toDoItemService.create(toDoItemDto));
    }

    @PutMapping
    public ResponseEntity<ToDoItemDto> update(@RequestBody ToDoItemDto toDoItemDto) {
        return ResponseEntity.ok(toDoItemService.update(toDoItemDto));
    }

    @PostMapping("/{id}/toggle")
    public ResponseEntity<ToDoItemDto> toggle(@PathVariable("id") Long id) {
        return ResponseEntity.ok(toDoItemService.toggle(id));
    }

}
