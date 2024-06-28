package com.luizgmelo.conduit.dtos;

import com.luizgmelo.conduit.models.User;

public record ResponseRegisterLoginDto(User user, String token) {
}
