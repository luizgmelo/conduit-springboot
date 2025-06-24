package com.luizgmelo.conduit.dtos;

import jakarta.validation.Valid;

public record RegisterRequestDto(@Valid UserRegisterDto user) {
}
