package com.luizgmelo.conduit.dtos;

import com.luizgmelo.conduit.models.UserProfile;

public record ArticleUpdateDto(
                String title,
                String description,
                String body,
                String[] tagList,
                boolean favorited,
                UserProfile author) {

}
