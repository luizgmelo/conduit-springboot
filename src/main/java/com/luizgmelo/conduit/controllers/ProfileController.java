package com.luizgmelo.conduit.controllers;

import com.luizgmelo.conduit.dtos.UserProfileResponseDTO;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.services.FollowService;
import com.luizgmelo.conduit.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles/{username}")
public class ProfileController {

    private final FollowService followService;
    private final UserService userService;

    public ProfileController(FollowService followService, UserService userService) {
        this.followService = followService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserProfileResponseDTO> getProfile(@AuthenticationPrincipal User user,
                                                             @PathVariable String username) {
        return ResponseEntity.ok(userService.getUserProfile(user, username));
    }

    @PostMapping("follow")
    public ResponseEntity<UserProfileResponseDTO> follow(@PathVariable String username) {
        User follower = userService.getAuthenticatedUser();
        User followed = userService.getUserByUsername(username);

        followService.follow(follower, followed);

        UserProfileResponseDTO response = UserProfileResponseDTO.fromProfile(followed, true);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("follow")
    public ResponseEntity<UserProfileResponseDTO> unfollow(@PathVariable String username) {
        User follower = userService.getAuthenticatedUser();
        User unfollowed = userService.getUserByUsername(username);

        followService.unfollow(follower, unfollowed);

        UserProfileResponseDTO response = UserProfileResponseDTO.fromProfile(unfollowed, false);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
