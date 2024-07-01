package com.luizgmelo.conduit.dtos;

import com.luizgmelo.conduit.models.User;

public record ResponseUserDto(User user, String token) {
}
