package com.example.todoapp.service;

import com.example.todoapp.dto.ToDoItemDto;
import com.example.todoapp.entity.ToDoItem;
import com.example.todoapp.repository.ToDoRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class ToDoItemService {

    @Autowired
    private ToDoRepository toDoRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<ToDoItemDto> list() {
        return toDoRepository.findAllByUsername(getCurrentUsername())
            .stream()
            .map(item -> modelMapper.map(item, ToDoItemDto.class))
            .collect(Collectors.toList());
    }

    public ToDoItemDto create(ToDoItemDto toDoItemDto) {
        ToDoItem item = toDoRepository.save(modelMapper.map(toDoItemDto, ToDoItem.class));
        return modelMapper.map(item, ToDoItemDto.class);
    }

    public ToDoItemDto update(ToDoItemDto toDoItemDto) {
        return null;
    }

    public ToDoItemDto toggle(Long id) {
        return null;
    }

    private String getCurrentUsername(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUsername();
    }
}
