package com.proyecto.SazonIA.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.SazonIA.service.ApiRecipeService;
import com.proyecto.SazonIA.spoonacularApi.ApiRecipe;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@Tag(name = "Api spoonacular", description = "Operations related to the spoonacular api")
public class ApiRecipeController {

    @Autowired
    private ApiRecipeService apiRecipeService;

    @CrossOrigin(origins = "http://localhost:4200")
    @Operation(summary = "Get a random recipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Random recipe found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiRecipe.class))),
            @ApiResponse(responseCode = "404", description = "No recipe found", content = @Content)
    })
    @GetMapping("/randomRecipes")
    public ResponseEntity<ApiRecipe> getRandomRecipe() {
        ApiRecipe randomRecipe = apiRecipeService.getRandomRecipe();
        if (randomRecipe != null) {
            return new ResponseEntity<>(randomRecipe, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}









