package com.luizgmelo.conduit.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record RegisterRequestDto(
        @Size(min = 5, max = 250, message = "Usernname must between 5 and 250 characters") String username,
        @Email String email,
        @Size(min = 8, max = 72, message = "Password msut between 8 and 72 characters") String password) {

}
