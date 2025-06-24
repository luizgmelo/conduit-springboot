package com.luizgmelo.conduit.dtos;

import com.luizgmelo.conduit.models.User;

public record UserProfileResponseDTO(UserProfileDTO user) {
    public static UserProfileResponseDTO fromProfile(User user, boolean isFollowed) {
        return new UserProfileResponseDTO(new UserProfileDTO(user.getUsername(),
                                                             user.getBio(),
                                                             user.getImage(),
                                                             isFollowed));
    }
}
