package com.proyecto.SazonIA.service;




import java.util.List;

import org.springframework.stereotype.Service;

import com.proyecto.SazonIA.spoonacularApi.ApiRecipe;


@Service
public class ApiRecipeResponse {
    
    private List<ApiRecipe> recipes;

    public List<ApiRecipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<ApiRecipe> recipes) {
        this.recipes = recipes;
    }
    
    
}




