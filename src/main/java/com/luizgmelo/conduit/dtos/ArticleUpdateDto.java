package com.luizgmelo.conduit.dtos;

import java.util.List;

public record ArticleUpdateDto(
                String title,
                String description,
                String body,
                List<String> tagList) {

}
