package com.luizgmelo.conduit.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.luizgmelo.conduit.models.User;

@Service
public class TokenService {
  @Value("${api.security.token.secret}")
  private String secret;

  public String generateToken(User user) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
            .withIssuer("conduit-project")
            .withIssuedAt(Instant.now())
            .withSubject(user.getEmail())
            .withExpiresAt(generateExpirationDate())
            .sign(algorithm);

    } catch (JWTCreationException ex) {
      throw new RuntimeException("Error while authenticating");
    }
  }

  public String validateToken(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);

      return JWT.require(algorithm)
          .withIssuer("conduit-project")
          .build()
          .verify(token)
          .getSubject();

    } catch (JWTVerificationException ex) {
      return null;
    }

  }

  private Instant generateExpirationDate() {
    return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
  }
}
