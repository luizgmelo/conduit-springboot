package com.luizgmelo.conduit.controllers;

import com.luizgmelo.conduit.dtos.UserAuthenticatedDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luizgmelo.conduit.dtos.ResponseUserDto;
import com.luizgmelo.conduit.dtos.UpdateUserRequestDto;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

  @Autowired
  UserService userService;

  @GetMapping
  public ResponseEntity<Object> getCurrentUser(@RequestHeader(name = "Authorization") String tokenBearer) {
    String token = tokenBearer.replace("Bearer ", "");
    UserAuthenticatedDTO response = userService.getUserAuthenticated(token);
    if (response != null)
      return ResponseEntity.status(HttpStatus.OK).body(response);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
  }

  @PutMapping
  public ResponseEntity<Object> updateCurrentUser(@RequestHeader(name = "Authorization") String tokenBearer,
      @RequestBody @Valid UpdateUserRequestDto body) {
    String token = tokenBearer.replace("Bearer ", "");
    User user = userService.updateCurrentUser(token, body);
    var response = new ResponseUserDto(user, token);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
