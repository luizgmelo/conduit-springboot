package com.luizgmelo.conduit.services;

import java.util.Optional;

import com.luizgmelo.conduit.dtos.UserAuthenticatedDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.luizgmelo.conduit.dtos.UpdateUserRequestDto;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.repositories.UserRepository;

@Service
public class UserService {
  @Autowired
  UserRepository userRepository;

  @Autowired
  TokenService tokenService;

  @Autowired
  PasswordEncoder passwordEncoder;

  public UserAuthenticatedDTO getUserAuthenticated(String token) {
    String email = tokenService.validateToken(token);
    Optional<User> userOpt = userRepository.findByEmail(email);
    User user = userOpt.get();
    var userAuthenticated = new UserAuthenticatedDTO(
            user.getEmail(),
            token,
            user.getUsername(),
            user.getBio(),
            user.getImage());

    return userAuthenticated;
  }

  public User updateCurrentUser(String token, UpdateUserRequestDto data) {
    String email = tokenService.validateToken(token);
    Optional<User> userOpt = userRepository.findByEmail(email);

    if (userOpt.isPresent()) {
      User user = userOpt.get();

      if (data.email() != null)
        user.setEmail(data.email());
      if (data.username() != null)
        user.setUsername(data.username());
      if (data.password() != null)
        user.setPassword(passwordEncoder.encode(data.password()));
      if (data.bio() != null)
        user.setBio(data.bio());
      if (data.image() != null)
        user.setImage(data.image());

      return userRepository.save(user);
    }
    return null;
  }
}
