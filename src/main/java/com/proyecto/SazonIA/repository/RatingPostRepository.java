package com.proyecto.SazonIA.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.SazonIA.model.RatingPost;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingPostRepository extends MongoRepository<RatingPost, String> {
    // Método para encontrar todas las valoraciones asociadas a un post específico
    List<RatingPost> findByPostId(String postId);
    Optional<RatingPost> findByPostIdAndUserId(String postId, Integer userId);
}
