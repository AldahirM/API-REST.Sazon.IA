package com.proyecto.SazonIA.service;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.proyecto.SazonIA.spoonacularApi.ApiRecipe;


@Service
public class ApiRecipeService {
    
    private final String apiKey = "8cf7724c75134bd284a067d001bac706";
    private final String apiUrl = "https://api.spoonacular.com/recipes/random";

    
    @SuppressWarnings("null")
    public ApiRecipe getRandomRecipe(){
        RestTemplate restTemplate = new RestTemplate();
        String url = apiUrl + "?apiKey=" + apiKey;
        ApiRecipeResponse response  = restTemplate.getForObject(url, ApiRecipeResponse.class);
        return response.getRecipes().get(0);  
    }
}

