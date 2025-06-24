package com.luizgmelo.conduit.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RequestArticleDTO(CreateArticleDto article) {}
