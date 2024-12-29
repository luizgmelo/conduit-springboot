package com.luizgmelo.conduit.exceptions;

public class UserProfileNotFoundException extends RuntimeException {
  public UserProfileNotFoundException() {
    super("User profile not found!");
  }
}
