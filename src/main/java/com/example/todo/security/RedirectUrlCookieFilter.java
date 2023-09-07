package com.example.todo.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Log4j2
public class RedirectUrlCookieFilter extends OncePerRequestFilter {

  public static final String REDIRECT_URI_PARAM = "redirect_url";
  private static final int MAX_AGE = 180;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    if(request.getRequestURI().startsWith("/auth/authorize")) {
      try {
        log.info("-----request uri: " + request.getRequestURI());
        String redirectUrl = request.getParameter(REDIRECT_URI_PARAM);

        Cookie cookie = new Cookie(REDIRECT_URI_PARAM, redirectUrl);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(MAX_AGE);
        response.addCookie(cookie);

      } catch (Exception e) {
        log.info("----------fail cookie: " + e);
      }
    }

    filterChain.doFilter(request,response);
  }
}
