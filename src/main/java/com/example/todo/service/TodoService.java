package com.example.todo.service;

import com.example.todo.model.TodoEntity;
import com.example.todo.persistence.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

  @Autowired
  private TodoRepository repository;

  public String testService() {

    TodoEntity entity = TodoEntity.builder().title("test title").build();
    repository.save(entity);

    TodoEntity savedEntity = repository.findById(entity.getId()).get();

    return savedEntity.getTitle();
  }
}
