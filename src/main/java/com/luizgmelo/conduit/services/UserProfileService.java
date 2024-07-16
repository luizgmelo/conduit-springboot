package com.luizgmelo.conduit.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luizgmelo.conduit.exceptions.UserProfileNotFoundException;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.models.UserProfile;
import com.luizgmelo.conduit.repositories.UserProfileRepository;
import com.luizgmelo.conduit.repositories.UserRepository;

@Service
public class UserProfileService implements IUserProfileService {

  @Autowired
  UserProfileRepository userProfileRepository;

  @Autowired
  UserRepository userRepository;

  @Override
  public UserProfile getProfile(String username) {
    Optional<UserProfile> userProfile = userProfileRepository.findByUsername(username);
    if (userProfile.isPresent()) {
      return userProfile.get();
    }
    throw new UserProfileNotFoundException("This user profile are not found!");
  }

  public void follow(User authenticatedUser, UserProfile profileToFollow) {
    authenticatedUser.getFollowing().add(profileToFollow);
    userRepository.save(authenticatedUser);
  }

  public void unfollow(User authenticatedUser, UserProfile profileToUnfollow) {
    authenticatedUser.getFollowing().remove(profileToUnfollow);
    userRepository.save(authenticatedUser);
  }

}
