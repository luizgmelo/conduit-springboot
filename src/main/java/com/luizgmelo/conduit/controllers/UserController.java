package com.luizgmelo.conduit.controllers;

import com.luizgmelo.conduit.dtos.AuthResponseDTO;
import com.luizgmelo.conduit.dtos.UpdateUserRequestDto;
import com.luizgmelo.conduit.dtos.UserDTO;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<AuthResponseDTO> getCurrentUser(@AuthenticationPrincipal User user) {
    UserDTO userDTO = new UserDTO(user.getEmail(), null, user.getUsername(), user.getBio(), user.getImage());
    AuthResponseDTO response = new AuthResponseDTO(userDTO);
    return ResponseEntity.ok(response);
  }

  @PutMapping
  public ResponseEntity<AuthResponseDTO> updateCurrentUser(@AuthenticationPrincipal User user,
                                                           @RequestBody @Valid UpdateUserRequestDto body) {
    return ResponseEntity.ok(userService.updateCurrentUser(user, body));
  }
}
