package com.luizgmelo.conduit.dtos;

public record UserAuthenticatedDTO(String email, String token, String username, String bio, String image) {
}
