CREATE TABLE favorites(
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    article_id UUID NOT NULL,
    CONSTRAINT fk_favorite_on_user
        FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_favorite_on_article
        FOREIGN KEY (article_id) REFERENCES articles(id)
);