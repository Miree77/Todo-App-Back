package com.example.todo.controller;

import com.example.todo.dto.ResponseDTO;
import com.example.todo.dto.TodoDTO;
import com.example.todo.model.TodoEntity;
import com.example.todo.service.TodoService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("todo")
@Log4j2
public class TodoController {

  @Autowired
  private TodoService service;

  @GetMapping("/test")
  public ResponseEntity<?> testTodo() {

    String str = service.testService();

    List<String> list = new ArrayList<>();
    list.add(str);

    ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
    return ResponseEntity.ok().body(response);

  }

  @PostMapping
  public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto, @AuthenticationPrincipal String userId) {

    try {

      TodoEntity entity = TodoDTO.toEntity(dto);

      entity.setId(null);
      entity.setUserId(userId);

      List<TodoEntity> entities = service.create(entity);
      List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

      ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

      return ResponseEntity.ok().body(response);


    } catch (Exception e) {

      String error = e.getMessage();
      ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();

      return ResponseEntity.badRequest().body(response);
    }

  }

  @GetMapping
  public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userId) {

    log.info("----------Get-------------------");

    List<TodoEntity> entities = service.retrieve(userId);

    List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

    ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

    return ResponseEntity.ok().body(response);

  }

  @PutMapping
  public ResponseEntity<?> updateTodo(@RequestBody TodoDTO dto, @AuthenticationPrincipal String userId) {

    TodoEntity entity = TodoDTO.toEntity(dto);
    entity.setUserId(userId);

    List<TodoEntity> entities = service.update(entity);
    List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

    ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

    return ResponseEntity.ok().body(response);
  }

  @DeleteMapping
  public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO dto, @AuthenticationPrincipal String userId) {

    try {

      TodoEntity entity = TodoDTO.toEntity(dto);
      entity.setUserId(userId);

      List<TodoEntity> entities = service.delete(entity);
      List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

      ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
      return ResponseEntity.ok().body(response);

    } catch (Exception e) {
      String error = e.getMessage();
      ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();

      return ResponseEntity.badRequest().body(response);
    }



  }
}
