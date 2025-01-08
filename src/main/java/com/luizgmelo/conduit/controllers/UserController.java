package com.luizgmelo.conduit.controllers;

import com.luizgmelo.conduit.dtos.AuthResponseDTO;
import com.luizgmelo.conduit.dtos.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luizgmelo.conduit.dtos.UpdateUserRequestDto;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<AuthResponseDTO> getCurrentUser() {
    User user = userService.getAuthenticatedUser();
    UserDTO userDTO = new UserDTO(user.getEmail(), null, user.getUsername(), user.getBio(), user.getImage());
    AuthResponseDTO response = new AuthResponseDTO(userDTO);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PutMapping
  public ResponseEntity<AuthResponseDTO> updateCurrentUser(@RequestHeader(name = "Authorization") String tokenBearer,
      @RequestBody @Valid UpdateUserRequestDto body) {
    String token = tokenBearer.replace("Bearer ", "");
    User user = userService.updateCurrentUser(token, body);

    UserDTO userDTO = new UserDTO(user.getEmail(), token, user.getUsername(), user.getBio(), user.getImage());
    var response = new AuthResponseDTO(userDTO);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
