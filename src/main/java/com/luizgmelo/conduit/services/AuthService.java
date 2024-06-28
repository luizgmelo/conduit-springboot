package com.luizgmelo.conduit.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.luizgmelo.conduit.dtos.LoginRequestDto;
import com.luizgmelo.conduit.dtos.RegisterRequestDto;
import com.luizgmelo.conduit.dtos.ResponseRegisterLoginDto;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AuthService implements IAuthService {
  @Autowired
  UserRepository userRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  TokenService tokenService;

  @Override
  public ResponseRegisterLoginDto login(LoginRequestDto body) {
    Optional<User> userOpt = userRepository.findByEmail(body.email());
    if (userOpt.isPresent()) {
      User user = userOpt.get();
      if (passwordEncoder.matches(body.password(), user.getPasswordHash())) {
        String token = tokenService.generateToken(user);
        return new ResponseRegisterLoginDto(user, token);
      }
    }
    throw new RuntimeException("Wrong email or password!");
  }

  @Override
  public ResponseRegisterLoginDto register(RegisterRequestDto body) {
    String passwordHash = passwordEncoder.encode(body.password());
    User newUser = new User(body.username(), body.email(), passwordHash);
    User user = userRepository.save(newUser);
    String token = tokenService.generateToken(user);
    return new ResponseRegisterLoginDto(user, token);
  }
}