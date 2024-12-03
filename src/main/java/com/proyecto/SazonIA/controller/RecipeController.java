package com.proyecto.SazonIA.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.proyecto.SazonIA.DTO.RecipeDTO;
import com.proyecto.SazonIA.model.Recipe;
import com.proyecto.SazonIA.model.User;
import com.proyecto.SazonIA.service.RecipeService;
import com.proyecto.SazonIA.service.UserService;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/recipes")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.DELETE })
@Tag(name = "Recipes from users", description = "Operations related to recipes in Sazón.IA")
public class RecipeController {

        @Autowired
        private RecipeService recipeService;

        @Autowired
        private UserService userService;

        @Autowired
        private ModelMapper modelMapper;
        
        private final Gson gson = new Gson();

        @Operation(summary = "Get recipes by pagination")
        @GetMapping(value = "pagination", params = { "page", "pageSize" })
        public List<RecipeDTO> getAllPagination(
                        @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
                List<Recipe> recipes = recipeService.getAll(page, pageSize);
                return recipes.stream()
                                .map(this::convertToDTO)
                                .collect(Collectors.toList());
        }

        @Operation(summary = "Get a recipe by Id")
        @ApiResponse(responseCode = "200", description = "The recipe has been found", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Recipe.class))) })
        @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Recipe.class))) })
        @ApiResponse(responseCode = "404", description = "The recipe was not found", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Recipe.class))) })
        @GetMapping("/{idRecipe}")
        public ResponseEntity<RecipeDTO> getById(@PathVariable Integer idRecipe) {
                return new ResponseEntity<>(convertToDTO(recipeService.getById(idRecipe)), HttpStatus.OK);
        }

        @Operation(summary = "Save a new recipe")
        @ApiResponse(responseCode = "200", description = "The recipe has saved", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Recipe.class))) })
        @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Recipe.class))) })
        @PostMapping(params = { "idUser" })
        public ResponseEntity<?> save(@RequestBody Recipe recipe,
                        @RequestParam(value = "idUser", required = true) Integer idUser) {
                User user = userService.getById(idUser);
                recipe.setUser(user);
                if (user == null) {
                        return new ResponseEntity<>(gson.toJson(Map.of("info", "User not found")), HttpStatus.NOT_FOUND);
                }
                recipe.setRecipe_time_stamp(Timestamp.valueOf(LocalDateTime.now()) + "");
                recipeService.save(recipe);
                return new ResponseEntity<>(gson.toJson(Map.of("info", "Recipe saved")), HttpStatus.OK);

        }

        @Operation(summary = "Update a recipe")
        @ApiResponse(responseCode = "200", description = "The recipe has been updated", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Recipe.class))) })
        @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Recipe.class))) })
        @ApiResponse(responseCode = "404", description = "The recipe was not found", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Recipe.class))) })
        @PutMapping("/{idRecipe}")
        public ResponseEntity<?> update(@RequestBody Recipe recipe,
                        @PathVariable Integer idRecipe) {
                Recipe aux = recipeService.getById(idRecipe);
                if (aux == null) {
                        return new ResponseEntity<>(gson.toJson(Map.of("error", "Recipe not found")), HttpStatus.NOT_FOUND);
                }
                User usAux = userService.getById(aux.getUser().getUser_id());
                recipe.setUser(usAux);
                recipe.setRecipe_time_stamp(aux.getRecipe_time_stamp());
                recipeService.save(recipe);
                return new ResponseEntity<>(gson.toJson(Map.of("info", "")), HttpStatus.OK);
        }

        @Operation(summary = "Delete a recipe by id")
        @ApiResponse(responseCode = "200", description = "The recipe has been found", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Recipe.class))) })
        @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Recipe.class))) })
        @ApiResponse(responseCode = "404", description = "The recipe was not found", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Recipe.class))) })
        @DeleteMapping("/{idRecipe}")
        public ResponseEntity<?> delete(@PathVariable Integer idRecipe) {
                Recipe recipe = recipeService.getById(idRecipe);
                if (recipe == null) {
                        return new ResponseEntity<>(gson.toJson(Map.of("error", "Recipe not found")), HttpStatus.NOT_FOUND);
                }
                recipeService.delete(idRecipe);
                return new ResponseEntity<>(gson.toJson(Map.of("info", "The recipe was deleted")), HttpStatus.OK);

        }

        @Operation(summary = "Get all recipes from a user paginated")
        @ApiResponse(responseCode = "200", description = "The recipes have been found", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Recipe.class))) })
        @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Recipe.class))) })
        @ApiResponse(responseCode = "404", description = "No recipes found", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Recipe.class))) })
        @GetMapping("/FromUser/{idUser}")
        public ResponseEntity<?> getRecipesByUser(@PathVariable Integer idUser,
                        @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
                User user = userService.getById(idUser);
                if (user == null) {
                        return new ResponseEntity<>(gson.toJson(Map.of("error", "User was not found")), HttpStatus.NOT_FOUND);
                }
                page = page * pageSize;
                List<Recipe> recipes = recipeService.getRecipesByUser(idUser, pageSize, page);
                return new ResponseEntity<>(recipes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()), HttpStatus.OK);
        }

        private RecipeDTO convertToDTO(Recipe recipe) {
                return modelMapper.map(recipe, RecipeDTO.class);
        }
}