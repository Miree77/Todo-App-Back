package com.example.todo.persistence;

import com.example.todo.model.TodoEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String> {

  List<TodoEntity> findByUserId(String userId);
}
