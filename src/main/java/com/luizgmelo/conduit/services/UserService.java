package com.luizgmelo.conduit.services;

import com.luizgmelo.conduit.dtos.AuthResponseDTO;
import com.luizgmelo.conduit.dtos.UpdateUserRequestDto;
import com.luizgmelo.conduit.dtos.UserDTO;
import com.luizgmelo.conduit.dtos.UserProfileResponseDTO;
import com.luizgmelo.conduit.exceptions.UserDetailsFailedException;
import com.luizgmelo.conduit.exceptions.UserNotFoundException;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final FollowService followService;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, FollowService followService) {
    this.userRepository = userRepository;
      this.passwordEncoder = passwordEncoder;
      this.followService = followService;
  }

  public UserProfileResponseDTO getUserProfile(User user, String username) {
    User profile = this.getUserByUsername(username);
    boolean isFollowed = followService.isFollowing(user, profile);
    return UserProfileResponseDTO.fromProfile(profile, isFollowed);
  }

  public UserProfileResponseDTO followUser(User user, String username) {
    User followed = this.getUserByUsername(username);
    followService.follow(user, followed);
    return UserProfileResponseDTO.fromProfile(followed, true);
  }

  public UserProfileResponseDTO unfollowUser(User user, String username) {
    User unfollowed = this.getUserByUsername(username);
    followService.unfollow(user, unfollowed);
    return UserProfileResponseDTO.fromProfile(unfollowed, false);
  }

  public User getAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Object principal = authentication.getPrincipal();
    if (principal instanceof User) {
      return (User) principal;
    }
    throw new UserDetailsFailedException();
  }

  public AuthResponseDTO updateCurrentUser(User user, UpdateUserRequestDto data) {
      if (data.user().email() != null)
        user.setEmail(data.user().email());
      if (data.user().username() != null)
        user.setUsername(data.user().username());
      if (data.user().password() != null)
        user.setPassword(passwordEncoder.encode(data.user().password()));
      if (data.user().bio() != null)
        user.setBio(data.user().bio());
      if (data.user().image() != null)
        user.setImage(data.user().image());

    User userUpdated = userRepository.save(user);
    UserDTO userDTO = new UserDTO(userUpdated.getEmail(), null, userUpdated.getUsername(), userUpdated.getBio(), userUpdated.getImage());
    return new AuthResponseDTO(userDTO);
  }

  public User getUserByUsername(String username) {
      return userRepository.findByUsername(username)
              .orElseThrow(() -> new UserNotFoundException(username + " not found"));
  }
}
