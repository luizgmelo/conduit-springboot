package com.luizgmelo.conduit.controllers;

import com.luizgmelo.conduit.dtos.UserProfileResponseDTO;
import com.luizgmelo.conduit.exceptions.UserDetailsFailedException;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.models.UserProfile;
import com.luizgmelo.conduit.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles/{username}")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserProfileResponseDTO> getProfile(@PathVariable String username) {
        User userAuthenticated = UserService.getAuthenticatedUser();
        if (userAuthenticated == null) {
            throw new UserDetailsFailedException();
        }
        UserProfile userAuthenticatedProfile = userService.getProfile(userAuthenticated.getUsername());
        UserProfile profile = userService.getProfile(username);
        // TODO Do a query in database instead of get all data and look for one profile
        boolean isFollowed = userAuthenticatedProfile.getFollowing().contains(profile);
        UserProfileResponseDTO response = UserProfileResponseDTO.fromProfile(profile, isFollowed);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("follow")
    public ResponseEntity<UserProfileResponseDTO> follow(@PathVariable String username) {
        User follower = UserService.getAuthenticatedUser();
        if (follower == null) {
            throw new UserDetailsFailedException();
        }
        UserProfile followingProfile = userService.followUser(follower.getUsername(), username);
        boolean isFollowed = true;
        UserProfileResponseDTO response = UserProfileResponseDTO.fromProfile(followingProfile, isFollowed);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("follow")
    public ResponseEntity<UserProfileResponseDTO> unfollow(@PathVariable String username) {
        User follower = UserService.getAuthenticatedUser();
        if (follower == null) {
            throw new UserDetailsFailedException();
        }
        UserProfile unfollowingProfile = userService.unfollowUser(follower.getUsername(), username);
        boolean isFollowed = false;
        UserProfileResponseDTO response = UserProfileResponseDTO.fromProfile(unfollowingProfile, isFollowed);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
