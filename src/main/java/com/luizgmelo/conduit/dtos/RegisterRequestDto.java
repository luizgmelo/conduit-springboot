package com.luizgmelo.conduit.dtos;

import jakarta.validation.constraints.*;

public record RegisterRequestDto(

        @NotEmpty @Size(min = 5, message = "Username must have min 5 characters") String username,
        @NotEmpty @Email String email,
        @NotEmpty @Size(min = 8, max = 72, message = "Password must between 8 and 72 characters") String password) {

}
