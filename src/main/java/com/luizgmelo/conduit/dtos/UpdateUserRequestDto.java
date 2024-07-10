package com.luizgmelo.conduit.dtos;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequestDto(
        @NotBlank(message = "Invalid email: must not be blank") String email,
        @NotBlank(message = "Invalid username: must not be blank") String username,
        @NotBlank(message = "Invalid password: must not be blank") String password,
        @NotBlank(message = "Invalid image: must not be blank") String image,
        @NotBlank(message = "Invalid bio: must not be blank") String bio) {
}
