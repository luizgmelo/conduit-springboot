package com.luizgmelo.conduit.controllers;

import com.luizgmelo.conduit.dtos.TagResponseDTO;
import com.luizgmelo.conduit.services.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<TagResponseDTO> listTags() {
        return ResponseEntity.ok(new TagResponseDTO(tagService.listTags()));
    }
}
