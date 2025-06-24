package com.luizgmelo.conduit.dtos;

import jakarta.validation.Valid;

public record LoginRequestDto(@Valid UserLoginDto user) {
}
