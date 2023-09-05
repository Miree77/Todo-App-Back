package com.example.todo.security;

import com.example.todo.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TokenProvider {

  private static final String SECRET_KEY = "VAsdfasvAsdfasdgADBVadGFadtfaevAdf23gdSFA3425tfgSDGFasdet45TYADfa";

  public String create(UserEntity userEntity) {
    // 기한은 1일
    Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

    return Jwts.builder()
        .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
        .setSubject(userEntity.getId())
        .setIssuer("todo App")
        .setIssuedAt(new Date())
        .setExpiration(expiryDate)
        .compact();
  }

  public String validateAndGetUserId(String token) {

    Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();

    return claims.getSubject();
  }
}
