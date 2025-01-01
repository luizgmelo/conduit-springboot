package com.luizgmelo.conduit.controllers;

import com.luizgmelo.conduit.dtos.TagResponseDTO;
import com.luizgmelo.conduit.services.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<TagResponseDTO> listTags() {
        List<String> tagList = tagService.listTags();
        TagResponseDTO response = new TagResponseDTO(tagList);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
