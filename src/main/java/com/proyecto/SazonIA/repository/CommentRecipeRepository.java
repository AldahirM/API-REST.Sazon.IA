package com.proyecto.SazonIA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto.SazonIA.model.CommentRecipe;
import java.util.List;

public interface CommentRecipeRepository extends JpaRepository<CommentRecipe, Integer> {

    @Query(value = "SELECT * FROM CommentRecipe WHERE recipe_id = :recipe_id LIMIT :page OFFSET :pageSize", nativeQuery = true)
    List<CommentRecipe> getCommentsByRecipe(@Param("recipe_id") Integer recipe_id, @Param("page") Integer page, @Param("pageSize") Integer pageSize);

}