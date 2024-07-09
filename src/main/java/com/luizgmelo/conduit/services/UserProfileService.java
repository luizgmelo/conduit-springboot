package com.luizgmelo.conduit.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luizgmelo.conduit.exceptions.UserProfileNotFoundException;
import com.luizgmelo.conduit.models.UserProfile;
import com.luizgmelo.conduit.repositories.UserProfileRepository;

@Service
public class UserProfileService implements IUserProfileService {

  @Autowired
  UserProfileRepository userProfileRepository;

  @Override
  public UserProfile getProfile(String username) {
    Optional<UserProfile> userProfile = userProfileRepository.findByUsername(username);
    if (userProfile.isPresent()) {
      return userProfile.get();
    }
    throw new UserProfileNotFoundException("This user profile are not found!");
  }

}
