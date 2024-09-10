package com.luizgmelo.conduit.models;

import java.util.*;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "articles")
@Getter
@Setter
@NoArgsConstructor
public class Article {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  private String slug;
  @Column(nullable = false, unique = true)
  private String title;
  @Column(nullable = false)
  private String description;
  @Column(nullable = false)
  private String body;
  @ElementCollection
  private ArrayList<String> tagList;
  private Date createdAt;
  private Date updatedAt;
  private boolean favorited;
  private int favoritesCounts;
  // <TO-DO> Do class Profile and attribute to Type of author
  private String author;

  @OneToMany(mappedBy = "commentFrom")
  private Set<Comment> comments;

  public Article(String title, String description, String body, ArrayList<String> tagList) {
    this.slug = generateSlug(title);
    this.title = title;
    this.description = description;
    this.body = body;
    this.tagList = tagList;
    this.createdAt = Calendar.getInstance().getTime();
    this.updatedAt = Calendar.getInstance().getTime();
  }

  private String generateSlug(String title) {
    return title.replace(" ", "-").toLowerCase();
  }

}
