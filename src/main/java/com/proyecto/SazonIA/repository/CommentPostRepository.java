package com.proyecto.SazonIA.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.SazonIA.model.CommentPost;

import java.util.List;

@Repository
public interface CommentPostRepository extends MongoRepository<CommentPost, String> {
    List<CommentPost> findByPostId(String postId);

    // Método para obtener comentarios paginados por postId
    Page<CommentPost> findByPostId(String postId, Pageable pageable);
    void deleteByPostId(String postId);
}
