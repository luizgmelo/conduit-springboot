package com.luizgmelo.conduit.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record UpdateUserRequestDto(
        @NotBlank(message = "Invalid email: must not be blank") @Email(message = "Email is invalid format") String email,
        @NotBlank(message = "Invalid username: must not be blank") @Size(min = 5, max = 30, message = "Username must between 5 and 30 characters") String username,
        @NotBlank(message = "Invalid password: must not be blank") @Size(min = 8, max = 72, message = "Password must between 8 and 72 characters") String password,
        @NotBlank(message = "Invalid image: must not be blank") @URL(message = "Image invalid url") String image,
        @NotBlank(message = "Invalid bio: must not be blank") @Size(max = 150, message = "Bio must have less than 500 characters")  String bio) {
}
