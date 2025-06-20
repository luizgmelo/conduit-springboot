CREATE TABLE articles_tags(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    article_id UUID NOT NULL,
    tag_id UUID NOT NULL,

    CONSTRAINT fk_articles_tags_on_article
        FOREIGN KEY (article_id) REFERENCES articles(id),
    CONSTRAINT fk_articles_tags_on_tags
        FOREIGN KEY (tag_id) REFERENCES tags(id)
);


