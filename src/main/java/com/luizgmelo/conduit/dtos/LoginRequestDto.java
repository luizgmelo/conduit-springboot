package com.luizgmelo.conduit.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(
    @NotBlank @Email String email,
    @NotBlank @Size(min = 8, max = 72, message = "Password must between 8 and 72 characters") String password) {
}
