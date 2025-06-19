package com.luizgmelo.conduit.dtos;

import jakarta.validation.constraints.*;

public record LoginRequestDto(
        @NotEmpty @Email String email,
        @NotEmpty @Size(min = 8, max = 72, message = "Password must between 8 and 72 characters") String password) {
}
