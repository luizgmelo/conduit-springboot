package com.luizgmelo.conduit.services;

import com.luizgmelo.conduit.models.UserProfile;
import com.luizgmelo.conduit.repositories.UserProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public UserProfile getProfile(String username) {
        return userProfileRepository.findUserProfileByUserUsername(username).orElse(null);
    }

}
