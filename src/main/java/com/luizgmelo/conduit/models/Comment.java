package com.luizgmelo.conduit.models;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "comments")
public class Comment {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  @Column(nullable = false)
  private String body;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "article_id", nullable = false)
  private Article commentFrom;

  public Comment() {
  }

  public Comment(String body, Article articleOwner) {
    this.body = body;
    this.commentFrom = articleOwner;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  public UUID getId() {
    return id;
  }

  public String getBody() {
    return body;
  }

  public Article getCommentFrom() {
    return commentFrom;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setCommentFrom(Article commentFrom) {
    this.commentFrom = commentFrom;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

}
