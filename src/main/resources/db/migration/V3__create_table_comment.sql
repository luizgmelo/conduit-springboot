CREATE TABLE comments(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    body TEXT NOT NULL,
    user_id UUID NOT NULL,
    article_id UUID NOT NULL,

    CONSTRAINT fk_comments_on_user
        FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_comments_from_article
        FOREIGN KEY (article_id) REFERENCES articles(id)
);

CREATE INDEX idx_comments_user_id ON comments(user_id);