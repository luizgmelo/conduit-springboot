package com.luizgmelo.conduit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luizgmelo.conduit.models.UserProfile;
import com.luizgmelo.conduit.services.UserProfileService;

@RestController
@RequestMapping("api/profiles/{username}")
public class UserProfileController {

  @Autowired
  UserProfileService userProfileService;

  @GetMapping
  public ResponseEntity getProfile(@PathVariable("username") String username) {
    UserProfile userProfile = userProfileService.getProfile(username);
    return ResponseEntity.status(HttpStatus.OK).body(userProfile);
  }

}
