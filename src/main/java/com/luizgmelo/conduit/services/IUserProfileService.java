package com.luizgmelo.conduit.services;

import com.luizgmelo.conduit.models.UserProfile;

public interface IUserProfileService {

  UserProfile getProfile(String username);

}
