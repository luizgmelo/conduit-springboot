package com.luizgmelo.conduit.services;

import java.util.Optional;

import com.luizgmelo.conduit.dtos.AuthResponseDTO;
import com.luizgmelo.conduit.dtos.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.luizgmelo.conduit.dtos.LoginRequestDto;
import com.luizgmelo.conduit.dtos.RegisterRequestDto;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.models.UserProfile;
import com.luizgmelo.conduit.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AuthService {
  @Autowired
  UserRepository userRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  TokenService tokenService;

  public AuthResponseDTO login(LoginRequestDto body) {
    Optional<User> userOpt = userRepository.findByEmail(body.email());
    if (userOpt.isPresent()) {
      User user = userOpt.get();
      if (passwordEncoder.matches(body.password(), user.getPassword())) {
        String token = tokenService.generateToken(user);
        UserDTO userDTO = new UserDTO(user.getEmail(), token, user.getUsername(), user.getBio(), user.getImage());
        return new AuthResponseDTO(userDTO);
      }
    }
    throw new RuntimeException("Wrong email or password!");
  }

  @Transactional
  public AuthResponseDTO register(RegisterRequestDto body) {
    String passwordHash = passwordEncoder.encode(body.password());

    User newUser = new User();
    newUser.setUsername(body.username());
    newUser.setEmail(body.email());
    newUser.setPassword(passwordHash);

    UserProfile userProfile = new UserProfile();
    userProfile.setUsername(body.username());

    newUser.setUserProfile(userProfile);

    User user = userRepository.save(newUser);
    String token = tokenService.generateToken(user);
    UserDTO userDTO = new UserDTO(user.getEmail(), token, user.getUsername(), user.getBio(), user.getImage());
    return new AuthResponseDTO(userDTO);
  }
}