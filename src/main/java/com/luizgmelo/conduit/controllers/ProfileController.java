package com.luizgmelo.conduit.controllers;

import com.luizgmelo.conduit.dtos.UserProfileResponseDTO;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles/{username}")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserProfileResponseDTO> getProfile(@AuthenticationPrincipal User user,
                                                             @PathVariable String username) {
        return ResponseEntity.ok(userService.getUserProfile(user, username));
    }

    @PostMapping("follow")
    public ResponseEntity<UserProfileResponseDTO> follow(@AuthenticationPrincipal User user,
                                                         @PathVariable String username) {
        return ResponseEntity.ok(userService.followUser(user, username));
    }

    @DeleteMapping("follow")
    public ResponseEntity<UserProfileResponseDTO> unfollow(@AuthenticationPrincipal User user,
                                                           @PathVariable String username) {
        return ResponseEntity.ok(userService.unfollowUser(user, username));
    }
}
