package com.luizgmelo.conduit.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Comment {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @Column(nullable = false)
  private String body;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "article_id")
  private Article commentFrom;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User author;

  public Comment(String body, Article article, User author) {
    this.body = body;
    this.commentFrom = article;
    this.author = author;
  }
}
