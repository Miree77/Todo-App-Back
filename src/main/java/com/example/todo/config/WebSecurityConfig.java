package com.example.todo.config;

import com.example.todo.security.JwtAuthenticationFilter;
import com.example.todo.security.OAuthSuccessHandler;
import com.example.todo.security.OAuthUserServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Log4j2
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Autowired
  private OAuthUserServiceImpl oAuthUserService;

  @Autowired
  private OAuthSuccessHandler oAuthSuccessHandler;

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.cors()
        .and()
        .csrf()
        .disable()
        .httpBasic()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/", "/auth/**", "/oauth2/**")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .oauth2Login()
        .redirectionEndpoint()
        .baseUri("/oauth2/**")
        .and()
        .userInfoEndpoint()
        .userService(oAuthUserService)   // OAuthUserServiceImpl 을 유저서비스로 등록
        .and()
        .successHandler(oAuthSuccessHandler);

        http.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class);
  }
}
