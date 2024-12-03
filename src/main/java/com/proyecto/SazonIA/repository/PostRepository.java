package com.proyecto.SazonIA.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.SazonIA.model.Post;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    // Método para obtener publicaciones paginadas por userId
    Page<Post> findByUserId(Integer userId, Pageable pageable);
    
}
