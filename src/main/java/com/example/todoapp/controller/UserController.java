package com.example.todoapp.controller;


import com.example.todoapp.dto.UserDto;
import com.example.todoapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/users/register")
    public ResponseEntity register(@RequestBody UserDto userDto){
        userService.register(userDto);
        return ResponseEntity.ok().build();
    }

}
