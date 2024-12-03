package com.proyecto.SazonIA.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.SazonIA.model.CommentRecipe;
import com.proyecto.SazonIA.repository.CommentRecipeRepository;
@Service
public class CommentRecipeService {
    @Autowired
    private CommentRecipeRepository commentRepository;
    
    public List<CommentRecipe> getAll() {
        return commentRepository.findAll();
    }

    public CommentRecipe getById(Integer commentId) {
        return commentRepository.findById(commentId).get();
    }

    public CommentRecipe save(CommentRecipe comment) {
        return commentRepository.save(comment);
    }

    public void delete(Integer commentId) {
        commentRepository.deleteById(commentId);
    }

    public List<CommentRecipe> getCommentsByRecipe(Integer recipeId, Integer page, Integer pageSize) {
        return commentRepository.getCommentsByRecipe(recipeId, page, pageSize);
    }

}