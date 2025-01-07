package com.luizgmelo.conduit.util;

import com.luizgmelo.conduit.models.Article;
import com.luizgmelo.conduit.models.Tag;
import com.luizgmelo.conduit.models.User;

import java.util.Set;

public class Constants {

    public static final User USER = new User("username", "email", "password");
    public static final Article ARTICLE = new Article("title", "title", "description", "body", Set.of(new Tag("tag")), USER);


}
