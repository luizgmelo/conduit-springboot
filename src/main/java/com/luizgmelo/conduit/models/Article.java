package com.luizgmelo.conduit.models;

import java.time.LocalDateTime;
import java.util.*;

import com.github.slugify.Slugify;
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

  @Column(nullable = false)
  private String slug;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private String body;

  @ElementCollection
  private List<String> tagList;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(nullable = false)
  private LocalDateTime updatedAt;

  private boolean isFavorite;

  private int favoritesCount;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User author;

  @OneToMany(mappedBy = "commentFrom")
  private Set<Comment> comments;

  public Article(String title, String description, String body, List<String> tagList, User author) {
    this.title = title;
    this.description = description;
    this.body = body;
    this.tagList = tagList;
    this.author = author;
  }

  @PrePersist
  @PreUpdate
  public void generateSlug() {
    Slugify slugify = Slugify.builder().build();
    this.slug = slugify.slugify(this.title);
  }
}
