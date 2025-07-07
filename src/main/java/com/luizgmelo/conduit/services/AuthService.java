package com.luizgmelo.conduit.services;

import com.luizgmelo.conduit.dtos.AuthResponseDTO;
import com.luizgmelo.conduit.dtos.LoginRequestDto;
import com.luizgmelo.conduit.dtos.RegisterRequestDto;
import com.luizgmelo.conduit.dtos.UserDTO;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.security.TokenService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

  private final PasswordEncoder passwordEncoder;
  private final TokenService tokenService;
  private final UserService userService;

  public AuthService(PasswordEncoder passwordEncoder, TokenService tokenService, UserService userService) {
    this.passwordEncoder = passwordEncoder;
    this.tokenService = tokenService;
    this.userService = userService;
  }

  public AuthResponseDTO login(LoginRequestDto body) {
    String email = body.user().email();
    String password = body.user().password();

    User user = userService.getUserByEmail(email)
            .orElseThrow(() -> new BadCredentialsException("Invalid email or password."));

    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new BadCredentialsException("Invalid email or password.");
    }

    String token = tokenService.generateToken(user);

    UserDTO userDTO = new UserDTO(user.getEmail(), token, user.getUsername(), user.getBio(), user.getImage());
    return new AuthResponseDTO(userDTO);
  }

  @Transactional
  public AuthResponseDTO register(RegisterRequestDto body) {
    String passwordHash = passwordEncoder.encode(body.user().password());

    User newUser = new User();
    newUser.setUsername(body.user().username());
    newUser.setEmail(body.user().email());
    newUser.setPassword(passwordHash);

    User user = userService.saveUser(newUser);
    String token = tokenService.generateToken(user);
    UserDTO userDTO = new UserDTO(user.getEmail(), token, user.getUsername(), user.getBio(), user.getImage());
    return new AuthResponseDTO(userDTO);
  }
}