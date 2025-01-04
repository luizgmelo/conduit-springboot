package com.luizgmelo.conduit.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(unique = true, nullable = false)
  private String username;
  @Column(unique = true, nullable = false)
  private String email;
  @Column(nullable = false)
  private String password;

  private String bio;
  private String image;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
  private Set<Comment> comments = new HashSet<>();

  @OneToMany(mappedBy = "follower")
  private Set<Follow> following = new HashSet<>();

  @OneToMany(mappedBy = "followed")
  private Set<Follow> followers = new HashSet<>();

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
          name = "favorites",
          joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "article_id")
  )
  private Set<Article> favoriteArticles = new HashSet<>();

  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }
}