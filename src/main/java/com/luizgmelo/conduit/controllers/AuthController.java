package com.luizgmelo.conduit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luizgmelo.conduit.dtos.LoginRequestDto;
import com.luizgmelo.conduit.dtos.RegisterRequestDto;
import com.luizgmelo.conduit.dtos.ResponseUserDto;
import com.luizgmelo.conduit.services.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class AuthController {

  @Autowired
  AuthService authService;

  @PostMapping("/login")
  public ResponseEntity login(@RequestBody @Valid LoginRequestDto body) {
    ResponseUserDto response = authService.login(body);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping("/register")
  public ResponseEntity register(@RequestBody @Valid RegisterRequestDto body) {
    ResponseUserDto response = authService.register(body);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
