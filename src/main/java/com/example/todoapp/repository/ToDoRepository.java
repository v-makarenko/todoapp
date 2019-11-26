package com.example.todoapp.repository;

import com.example.todoapp.entity.ToDoItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToDoRepository extends JpaRepository<ToDoItem, Long> {

    List<ToDoItem> findAllByUsername(String username);
}
