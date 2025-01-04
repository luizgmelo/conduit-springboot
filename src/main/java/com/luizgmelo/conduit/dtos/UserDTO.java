package com.luizgmelo.conduit.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

public record UserDTO(String email, @JsonInclude(JsonInclude.Include.NON_NULL) String token, String username, String bio, String image) {
}
