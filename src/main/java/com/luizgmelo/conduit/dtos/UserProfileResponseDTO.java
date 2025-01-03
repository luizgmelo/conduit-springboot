package com.luizgmelo.conduit.dtos;

import com.luizgmelo.conduit.models.UserProfile;

public record UserProfileResponseDTO(UserProfileDTO profile) {
    public static UserProfileResponseDTO fromProfile(UserProfile userProfile, boolean isFollowed) {
        return new UserProfileResponseDTO(new UserProfileDTO(userProfile.getUser().getUsername(),
                                                             userProfile.getUser().getBio(),
                                                             userProfile.getUser().getImage(),
                                                             isFollowed));
    }
}
