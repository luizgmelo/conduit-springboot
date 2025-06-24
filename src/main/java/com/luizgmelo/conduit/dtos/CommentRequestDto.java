package com.luizgmelo.conduit.dtos;

import jakarta.validation.Valid;

public record CommentRequestDto(@Valid CommentDto comment) {

}
