package com.luizgmelo.conduit.dtos;

import jakarta.validation.Valid;

public record UpdateUserRequestDto(@Valid UserUpdateDto user) {
}
