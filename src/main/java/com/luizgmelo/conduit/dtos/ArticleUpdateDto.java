package com.luizgmelo.conduit.dtos;

public record ArticleUpdateDto(
                String title,
                String description,
                String body,
                String[] tagList,
                Boolean favorited) {

}
