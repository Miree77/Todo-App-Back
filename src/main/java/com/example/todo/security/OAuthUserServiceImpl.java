package com.example.todo.security;

import com.example.todo.model.UserEntity;
import com.example.todo.persistence.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OAuthUserServiceImpl extends DefaultOAuth2UserService {

  @Autowired
  private UserRepository userRepository;

  public OAuthUserServiceImpl() {
    super();
  }

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

    //DefaultOAuth2UserService의 기존 loadUser를 호출. 이 메서드가 user-info-uri를 이용해 사용자 정보를 가져오는 부분
    final OAuth2User oAuth2User = super.loadUser(userRequest);

    try {
      // 사용자 정보 테스트 시에만 사용해야 함
      log.info("--------OAuth2User attributes {} " + new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
    } catch (Exception e) {
      e.printStackTrace();
    }

    // login 필드 가져옴
    final String username = (String) oAuth2User.getAttributes().get("login");
    final String authProvider = userRequest.getClientRegistration().getClientName();

    UserEntity userEntity = null;

    if (!userRepository.existsByUsername(username)) {
      userEntity = UserEntity.builder().username(username).authPrivider(authProvider).build();
      userEntity = userRepository.save(userEntity);
    }

    log.info("-----------username {} authProvider {} ", username, authProvider);

    return oAuth2User;

  }
}
