package com.luizgmelo.conduit.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.models.UserProfile;
import com.luizgmelo.conduit.services.UserProfileService;
import com.luizgmelo.conduit.services.UserService;
// TODO returns should be DTOS
@RestController
@RequestMapping("api/profiles/{username}")
public class UserProfileController {

  private final UserService userService;

  private final UserProfileService userProfileService;

  public UserProfileController(UserService userService, UserProfileService userProfileService) {
    this.userService = userService;
    this.userProfileService = userProfileService;
  }

  @GetMapping
  public ResponseEntity<UserProfile> getProfile(@PathVariable("username") String username) {
    UserProfile userProfile = userProfileService.getProfile(username);
    return ResponseEntity.status(HttpStatus.OK).body(userProfile);
  }

  @PostMapping("follow")
  public ResponseEntity<UserProfile> follow(@PathVariable("username") String username) {
    User currentUser = userService.getAuthenticatedUser();
    UserProfile profileToFollow = userProfileService.getProfile(username);
    userProfileService.follow(currentUser, profileToFollow);
    return ResponseEntity.status(HttpStatus.OK).body(profileToFollow);
  }

  @DeleteMapping("follow")
  public ResponseEntity<UserProfile> unfollow(@PathVariable("username") String username) {
    User currentUser = userService.getAuthenticatedUser();
    UserProfile profileToUnfollow = userProfileService.getProfile(username);
    userProfileService.unfollow(currentUser, profileToUnfollow);
    return ResponseEntity.status(HttpStatus.OK).body(profileToUnfollow);
  }

}
