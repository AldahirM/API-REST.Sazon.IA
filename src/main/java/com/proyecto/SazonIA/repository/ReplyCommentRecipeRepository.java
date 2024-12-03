package com.proyecto.SazonIA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto.SazonIA.model.ReplyCommentRecipe;
import java.util.List;

public interface ReplyCommentRecipeRepository  extends JpaRepository<ReplyCommentRecipe, Integer>{

    @Query (value = "SELECT * FROM ReplyCommentRecipe WHERE comment_id = :comment_id LIMIT :page OFFSET :pageSize", nativeQuery = true)
    List<ReplyCommentRecipe> getRepliesByComment(@Param("comment_id") Integer comment_id, @Param("page") Integer page, @Param("pageSize") Integer pageSize);

}
