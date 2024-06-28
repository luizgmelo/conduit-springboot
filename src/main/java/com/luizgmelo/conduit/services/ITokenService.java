package com.luizgmelo.conduit.services;

import com.luizgmelo.conduit.models.User;

public interface ITokenService {
  String generateToken(User user);

  String validateToken(String token);
}
