package com.luizgmelo.conduit.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(
        @NotBlank @NotNull @Email String email,
        @NotBlank @NotNull @Size(min = 8, max = 72, message = "Password must between 8 and 72 characters") String password) {
}
