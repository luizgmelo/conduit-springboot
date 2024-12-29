package com.luizgmelo.conduit.dtos;

import com.luizgmelo.conduit.models.User;

public record AuthorDTO(String username, String bio, String image, boolean following) {
    public static AuthorDTO fromAuthor(User author) {
        return new AuthorDTO(author.getUsername(), author.getBio(), author.getImage(), false);
    }
}
