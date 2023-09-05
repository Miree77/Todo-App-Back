package com.example.todo.service;

import com.example.todo.model.UserEntity;
import com.example.todo.persistence.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class UserService {
  @Autowired
  private UserRepository userRepository;

  public UserEntity create(final UserEntity userEntity) {

    if (userEntity == null || userEntity.getUsername() == null) {
      throw new RuntimeException("Invalid Argument");
    }

    final String username  = userEntity.getUsername();
    if (userRepository.existsByUsername(username)) {
      log.warn("------Username already exists {}", username);
      throw new RuntimeException("Username already exists");
    }

    return userRepository.save(userEntity);
  }

  public UserEntity getByCredentials(final String username, final String password, final
      PasswordEncoder encoder) {
    final UserEntity originalUser = userRepository.findByUsername(username);

    if (originalUser != null && encoder.matches(password, originalUser.getPassword())) {
      return originalUser;
    }
    return null;
  }
}
