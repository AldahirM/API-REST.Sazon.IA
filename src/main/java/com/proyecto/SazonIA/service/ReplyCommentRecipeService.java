package com.proyecto.SazonIA.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.SazonIA.model.ReplyCommentRecipe;
import com.proyecto.SazonIA.repository.ReplyCommentRecipeRepository;

@Service
public class ReplyCommentRecipeService {

    @Autowired
    private ReplyCommentRecipeRepository replyCommentRepository;

    public List<ReplyCommentRecipe> getAll() {
        return replyCommentRepository.findAll();
    }

    public ReplyCommentRecipe getById(Integer replyCommentId) {
        return replyCommentRepository.findById(replyCommentId).get();
    }

    public void save(ReplyCommentRecipe replyComment) {
        replyCommentRepository.save(replyComment);
    }

    public void delete(Integer replyCommentId) {
        replyCommentRepository.deleteById(replyCommentId);
    }

    public List<ReplyCommentRecipe> getRepliesByComment(Integer commentId, Integer page, Integer pageSize) {
        return replyCommentRepository.getRepliesByComment(commentId, page, pageSize);
    }

}
