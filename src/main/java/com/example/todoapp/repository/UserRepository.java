package com.example.todoapp.repository;

import com.example.todoapp.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByUsername(String username);
}
