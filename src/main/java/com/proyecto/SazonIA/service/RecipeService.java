package com.proyecto.SazonIA.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.proyecto.SazonIA.model.Recipe;
import com.proyecto.SazonIA.repository.RecipeRepository;


@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public List<Recipe> getAll(int page, int pageSize){
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return recipeRepository.findAll(pageRequest).getContent();
    }

    public Recipe getById(Integer recipeId) {
        return recipeRepository.findById(recipeId).get();
    }

    public Recipe save(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public void delete(Integer recipeId) {
        recipeRepository.deleteById(recipeId);
    }

    public List<Recipe> getRecipesByUser(Integer userId, Integer page, Integer pageSize) {
        return recipeRepository.getRecipesByUser(userId, page, pageSize);
    }
    
}