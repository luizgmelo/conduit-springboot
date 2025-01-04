package com.luizgmelo.conduit.services;

import java.util.Optional;

import com.luizgmelo.conduit.exceptions.UserDetailsFailedException;
import com.luizgmelo.conduit.exceptions.UserNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.luizgmelo.conduit.dtos.UpdateUserRequestDto;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.repositories.UserRepository;

@Service
public class UserService {
  private final UserRepository userRepository;

  private final TokenService tokenService;

  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, TokenService tokenService, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.tokenService = tokenService;
    this.passwordEncoder = passwordEncoder;
  }

  public User getAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Object principal = authentication.getPrincipal();
    if (principal instanceof User) {
      return (User) principal;
    }
    throw new UserDetailsFailedException();
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

  public User getUserByUsername(String username) {
      return userRepository.findByUsername(username)
              .orElseThrow(() -> new UserNotFoundException(username + " not found"));
  }
}
