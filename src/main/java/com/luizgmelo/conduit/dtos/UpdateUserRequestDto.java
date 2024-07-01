package com.luizgmelo.conduit.dtos;

public record UpdateUserRequestDto(String email, String username, String password, String image, String bio) {

}
