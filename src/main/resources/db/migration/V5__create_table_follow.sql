CREATE TABLE follows(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    follower_id UUID NOT NULL,
    followed_id UUID NOT NULL,

    CONSTRAINT fk_follower_on_user
        FOREIGN KEY (follower_id) REFERENCES users(id),
    CONSTRAINT fk_followed_on_user
        FOREIGN KEY (followed_id) REFERENCES users(id)
);