package com.proyecto.SazonIA.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.SazonIA.model.RatingCommentPost;

import java.util.List;

@Repository
public interface RatingCommentPostRepository extends MongoRepository<RatingCommentPost, String> {
    // Método para encontrar todas las valoraciones asociadas a un comentario específico
    List<RatingCommentPost> findByCommentId(String commentId);
    List<RatingCommentPost> findByCommentIdAndUserId(String commentId, Integer userId);

}
