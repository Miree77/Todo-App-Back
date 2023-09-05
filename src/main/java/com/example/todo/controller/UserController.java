package com.example.todo.controller;

import com.example.todo.dto.ResponseDTO;
import com.example.todo.dto.UserDTO;
import com.example.todo.model.UserEntity;
import com.example.todo.security.TokenProvider;
import com.example.todo.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Log4j2
public class UserController {

  @Autowired
  private UserService userService;
  @Autowired
  private TokenProvider tokenProvider;

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
    try {
      if (userDTO == null || userDTO.getPassword() == null) {
        throw new RuntimeException("Invalid Password value");
      }
      UserEntity user = UserEntity.builder().username(userDTO.getUsername())
          .password(userDTO.getPassword()).build();

      UserEntity registeredUser = userService.create(user);
      UserDTO responseUserDTO = UserDTO.builder().id(registeredUser.getId())
          .username(registeredUser.getUsername()).build();

      return ResponseEntity.ok().body(responseUserDTO);

    } catch (Exception e) {
      ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
      return ResponseEntity.badRequest().body(responseDTO);
    }
  }

  @PostMapping("/signin")
  public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {

    UserEntity user = userService.getByCredentials(userDTO.getUsername(), userDTO.getPassword());
    if (user != null) {
      //token 생성
      final String token = tokenProvider.create(user);
      final UserDTO responseUserDTO = UserDTO.builder().username(user.getUsername())
          .id(user.getId()).token(token).build();

      return ResponseEntity.ok().body(responseUserDTO);
    } else {
      ResponseDTO responseDTO = ResponseDTO.builder().error("Login failed.").build();

      return ResponseEntity.badRequest().body(responseDTO);
    }


  }
}
