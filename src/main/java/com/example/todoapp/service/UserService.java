package com.example.todoapp.service;

import com.example.todoapp.config.security.UserRole;
import com.example.todoapp.dto.UserDto;
import com.example.todoapp.entity.User;
import com.example.todoapp.repository.UserRepository;
import java.util.Arrays;
import javax.ws.rs.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()).size() > 0) {
            throw new BadRequestException("Username is already registered");
        }
        if (Arrays.equals(userDto.getUsername().toCharArray(), userDto.getPassword())) {
            throw new BadRequestException("Username and password could not be the same");
        } else {
            User user = modelMapper.map(userDto, User.class);
            user.setRole(UserRole.ROLE_USER);
            user.setPassword(passwordEncoder.encode(new String(userDto.getPassword())).toCharArray());
            userRepository.save(user);
        }
    }
}
