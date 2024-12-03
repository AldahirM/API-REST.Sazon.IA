package com.proyecto.SazonIA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.SazonIA.model.FavoritePost;
import com.proyecto.SazonIA.model.FavoritePostId;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface FavoritePostRepository extends JpaRepository<FavoritePost, FavoritePostId> {
    Page<FavoritePost> findByIdUserId(Integer userId, Pageable pageable);    
}
