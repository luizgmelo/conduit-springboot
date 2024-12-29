package com.luizgmelo.conduit.controllers;

import com.luizgmelo.conduit.dtos.UserProfileResponseDTO;
import com.luizgmelo.conduit.exceptions.UserProfileNotFoundException;
import com.luizgmelo.conduit.models.UserProfile;
import com.luizgmelo.conduit.services.UserProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profiles/")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    // TODO return true if follow this user else return false in ResponseDTO
    @GetMapping("{username}")
    public ResponseEntity<UserProfileResponseDTO> getProfile(@PathVariable String username) {
        UserProfile profile = userProfileService.getProfile(username);
        if (profile == null) {
            throw new UserProfileNotFoundException();
        }

        UserProfileResponseDTO response = UserProfileResponseDTO.fromProfile(profile);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
