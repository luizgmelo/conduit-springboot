CREATE TABLE articles(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    slug VARCHAR(255) UNIQUE NOT NULL,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    favorites_count int,
    user_id UUID NOT NULL,

    CONSTRAINT fk_comments_on_user
        FOREIGN KEY (user_id) REFERENCES users(id)
);