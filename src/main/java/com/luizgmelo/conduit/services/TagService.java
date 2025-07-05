package com.luizgmelo.conduit.services;

import com.luizgmelo.conduit.models.Tag;
import com.luizgmelo.conduit.repositories.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<String> listTags() {
        return tagRepository.findAll()
                .stream().map(Tag::getName)
                .toList();
    }

    public Tag getOrSaveTagByName(String name) {
        return tagRepository.findByName(name)
                .orElseGet(() -> tagRepository.save(new Tag(name)));
    }
}

