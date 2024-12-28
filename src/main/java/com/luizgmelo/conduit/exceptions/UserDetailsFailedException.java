package com.luizgmelo.conduit.exceptions;

public class UserDetailsFailedException extends RuntimeException {
    public UserDetailsFailedException() {
        super("Failed to read user authenticated details.");
    }
}
