package com.luizgmelo.conduit.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luizgmelo.conduit.models.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

}
