package com.proyecto.SazonIA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.proyecto.SazonIA.model.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

    @Query(value = "SELECT * FROM Recipe WHERE user_id = :user_id LIMIT :page OFFSET :pageSize", nativeQuery = true)
    List<Recipe> getRecipesByUser(@Param("user_id") Integer user_id, @Param("page") Integer page, @Param("pageSize") Integer pageSize);

}