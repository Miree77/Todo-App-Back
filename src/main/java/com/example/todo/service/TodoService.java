package com.example.todo.service;

import com.example.todo.model.TodoEntity;
import com.example.todo.persistence.TodoRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class TodoService {

  @Autowired
  private TodoRepository repository;

  public String testService() {

    TodoEntity entity = TodoEntity.builder().title("test title").build();
    repository.save(entity);

    TodoEntity savedEntity = repository.findById(entity.getId()).get();

    return savedEntity.getTitle();
  }

  public List<TodoEntity> create(final TodoEntity entity) {

    validate(entity);

    repository.save(entity);
    log.info("Entity id: {} is saved", entity.getId());

    return repository.findByUserId(entity.getUserId());
  }

  public List<TodoEntity> retrieve(final String userId) {
    return repository.findByUserId(userId);
  }

  public List<TodoEntity> update(final TodoEntity entity) {

    validate(entity);

    final Optional<TodoEntity> original = repository.findById(entity.getId());

    original.ifPresent(todo -> {

      todo.setTitle(entity.getTitle());
      todo.setDone(entity.isDone());

      repository.save(todo);
    });

    return retrieve(entity.getUserId());

  }

  public List<TodoEntity> delete(final TodoEntity entity) {

    validate(entity);

    try {

      repository.delete(entity);

    } catch (Exception e) {

      log.error("--- error delete: ", entity.getId(), e);

      throw new RuntimeException("error delete" + entity.getId());
    }

    return retrieve(entity.getUserId());
  }

  private void validate(final TodoEntity entity) {
    //validation
    if (entity == null) {
      log.warn("--------Entity null");
      throw new RuntimeException("Entity NUll");
    }

    if (entity.getUserId() == null) {
      log.warn("--------Unknown User");
      throw new RuntimeException("Unknown User");
    }
  }
}
