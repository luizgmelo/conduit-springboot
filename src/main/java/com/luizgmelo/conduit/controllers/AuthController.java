package com.luizgmelo.conduit.controllers;

import com.luizgmelo.conduit.dtos.AuthResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luizgmelo.conduit.dtos.LoginRequestDto;
import com.luizgmelo.conduit.dtos.RegisterRequestDto;
import com.luizgmelo.conduit.services.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginRequestDto dto) {
    return ResponseEntity.ok(authService.login(dto));
  }

  @PostMapping
  public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid RegisterRequestDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(dto));
  }
}
