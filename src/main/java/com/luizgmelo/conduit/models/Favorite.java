package com.luizgmelo.conduit.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "favorites")
@Getter
@Setter
@NoArgsConstructor
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    public Favorite(User user, Article article) {
        this.user = user;
        this.article = article;
    }
}
