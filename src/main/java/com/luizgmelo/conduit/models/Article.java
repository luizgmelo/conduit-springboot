package com.luizgmelo.conduit.models;

import java.time.LocalDateTime;
import java.util.*;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "articles")
@EntityListeners(AuditingEntityListener.class)
public class Article {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(unique = true, nullable = false)
  private String slug;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private String body;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(nullable = false)
  private LocalDateTime updatedAt;

  private int favoritesCount;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User author;

  @OneToMany(mappedBy = "commentFrom")
  private Set<Comment> comments;

  @ManyToMany(mappedBy = "favoriteArticles")
  private Set<UserProfile> usersWhoFavorited = new HashSet<>();

  @ManyToMany(cascade = CascadeType.PERSIST)
  @JoinTable(
          name = "articles_tags",
          joinColumns = @JoinColumn(name = "article_id"),
          inverseJoinColumns = @JoinColumn(name = "tag_id")
  )
  private Set<Tag> tags = new HashSet<>();

  public Article(String slug, String title, String description, String body, Set<Tag> tags, User author) {
    this.slug = slug;
    this.title = title;
    this.description = description;
    this.body = body;
    this.tags = tags;
    this.author = author;
  }

  public List<String> getTagList() {
    return this.getTags()
            .stream()
            .map(Tag::getName)
            .toList();
  }
}
