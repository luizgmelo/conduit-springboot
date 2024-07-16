package com.luizgmelo.conduit.models;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "articles")
public class Article {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  private String slug;
  @Column(nullable = false)
  private String title;
  @Column(nullable = false)
  private String description;
  @Column(nullable = false)
  private String body;
  private String[] tagList;
  private Date createdAt;
  private Date updatedAt;
  private boolean favorited;
  private Integer favoritesCounts;
  private UserProfile author;

  @OneToMany(mappedBy = "commentFrom")
  private Set<Comment> comments;

  public Article() {
  }

  public Article(UserProfile author) {
    this.author = author;
  }

  public Article(String title, String description, String body, String[] tagList) {
    this.slug = title.replace(" ", "-").toLowerCase();
    this.title = title;
    this.description = description;
    this.body = body;
    this.tagList = tagList;
    this.createdAt = Calendar.getInstance().getTime();
    this.updatedAt = Calendar.getInstance().getTime();
    this.favorited = false;
    this.favoritesCounts = 0;
  }

  public UUID getId() {
    return id;
  }

  public String getSlug() {
    return slug;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String getBody() {
    return body;
  }

  public String[] getTagList() {
    return tagList;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public boolean isFavorited() {
    return favorited;
  }

  public Integer getFavoritesCounts() {
    return favoritesCounts;
  }

  public UserProfile getAuthor() {
    return author;
  }

  public Set<Comment> getComments() {
    return comments;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public void setTagList(String[] tagList) {
    this.tagList = tagList;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  public void setFavorited(boolean favorited) {
    this.favorited = favorited;
  }

  public void setFavoritesCounts(Integer favoritesCounts) {
    this.favoritesCounts = favoritesCounts;
  }

  public void setAuthor(UserProfile author) {
    this.author = author;
  }

  public void setComments(Set<Comment> comments) {
    this.comments = comments;
  }
}
