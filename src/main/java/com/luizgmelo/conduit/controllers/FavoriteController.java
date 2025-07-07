package com.luizgmelo.conduit.controllers;

import com.luizgmelo.conduit.dtos.ArticleResponseDTO;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.services.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles/{slug}/favorite")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<ArticleResponseDTO> addFavorite(@AuthenticationPrincipal User user,
                                                          @PathVariable String slug) {
        return ResponseEntity.ok(favoriteService.addFavorite(user , slug));
    }

    @DeleteMapping
    public ResponseEntity<ArticleResponseDTO> removeFavorite(@AuthenticationPrincipal User user,
                                                             @PathVariable String slug) {
        return ResponseEntity.ok(favoriteService.removeFavorite(user , slug));
    }
}
