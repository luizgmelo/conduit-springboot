package com.luizgmelo.conduit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.models.UserProfile;
import com.luizgmelo.conduit.services.UserProfileService;
import com.luizgmelo.conduit.services.UserService;

@RestController
@RequestMapping("api/profiles/{username}")
public class UserProfileController {

  @Autowired
  UserService userService;

  @Autowired
  UserProfileService userProfileService;

  @GetMapping
  public ResponseEntity getProfile(@PathVariable("username") String username) {
    UserProfile userProfile = userProfileService.getProfile(username);
    return ResponseEntity.status(HttpStatus.OK).body(userProfile);
  }

  @PostMapping("follow")
  public ResponseEntity follow(@RequestHeader("Authorization") String tokenBearer,
      @PathVariable("username") String username) {
    String token = tokenBearer.replace("Bearer ", "");
    User currentUser = userService.getUserAuthenticated(token);
    UserProfile profileToFollow = userProfileService.getProfile(username);
    userProfileService.follow(currentUser, profileToFollow);
    return ResponseEntity.status(HttpStatus.OK).body(profileToFollow);
  }

  @DeleteMapping("follow")
  public ResponseEntity unfollow(@RequestHeader("Authorization") String tokenBearer,
      @PathVariable("username") String username) {
    String token = tokenBearer.replace("Bearer ", "");
    User currentUser = userService.getUserAuthenticated(token);
    UserProfile profileToUnfollow = userProfileService.getProfile(username);
    userProfileService.unfollow(currentUser, profileToUnfollow);
    return ResponseEntity.status(HttpStatus.OK).body(profileToUnfollow);
  }

}
