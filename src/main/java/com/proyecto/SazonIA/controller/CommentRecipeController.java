package com.proyecto.SazonIA.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.modelmapper.ModelMapper;
import java.util.Map;
// import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.proyecto.SazonIA.DTO.CommentRecipeDTO;
import com.proyecto.SazonIA.model.CommentRecipe;
import com.proyecto.SazonIA.service.CommentRecipeService;
import com.proyecto.SazonIA.service.RecipeService;
import com.proyecto.SazonIA.service.UserService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/commentsRecipes")
@CrossOrigin(origins = "*")
@Tag(name = "Comments from recipes", description = "Operations related to Comments in a recipe in Sazón.IA")
public class CommentRecipeController {
        @Autowired
        private CommentRecipeService commentService;

        @Autowired
        private RecipeService recipeService;

        @Autowired
        private UserService userService;

        @Autowired
        private ModelMapper modelMapper;

        private final Gson gson = new Gson();

        @Operation(summary = "Get a comment by Id")
        @ApiResponse(responseCode = "200", description = "The comment has been found", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentRecipe.class))) })
        @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentRecipe.class))) })
        @ApiResponse(responseCode = "404", description = "No comments registered", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentRecipe.class))) })
        @GetMapping("/{idComment}")
        public ResponseEntity<CommentRecipeDTO> getById(
                        @PathVariable Integer idComment) {
                return new ResponseEntity<>(convertToDTO(commentService.getById(idComment)), HttpStatus.OK);
        }

        @Operation(summary = "Save a new comment")
        @ApiResponse(responseCode = "200", description = "The comment has been saved", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentRecipe.class))) })
        @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentRecipe.class))) })
        @PostMapping(params = { "idRecipe", "idUser" })
        public ResponseEntity<?> save(@RequestBody CommentRecipe comment,
                        @RequestParam(value = "idRecipe", required = true) Integer idRecipe,
                        @RequestParam(value = "idUser", required = true) Integer idUser) {
                comment.setComment_time_stamp(Timestamp.valueOf(LocalDateTime.now()) + "");
                comment.setUser(userService.getById(idUser));
                if (userService.getById(idUser) == null) {
                        return new ResponseEntity<>(gson.toJson(Map.of("error", "User not found")), HttpStatus.NOT_FOUND);

                }
                comment.setRecipe(recipeService.getById(idRecipe));
                if (recipeService.getById(idRecipe) == null) {
                        return new ResponseEntity<>(gson.toJson(Map.of("error", "Recipe not found")), HttpStatus.NOT_FOUND);
                }
                commentService.save(comment);
                return new ResponseEntity<>(gson.toJson(Map.of("info", "Comment saved")), HttpStatus.OK);

        }

        @Operation(summary = "Update an existing comment by Id")
        @ApiResponse(responseCode = "200", description = "The comment has been updated", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentRecipe.class))) })
        @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentRecipe.class))) })
        @ApiResponse(responseCode = "404", description = "The comment was not found", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentRecipe.class))) })
        @PutMapping("/{idComment}")
        public ResponseEntity<?> update(@PathVariable Integer idComment,
                        @RequestBody CommentRecipe comment) {
                CommentRecipe aux = commentService.getById(idComment);
                comment.setUser(aux.getUser());
                if (commentService.getById(idComment) == null) {
                        return new ResponseEntity<>(gson.toJson(Map.of("error", "Comment not found")), HttpStatus.NOT_FOUND);

                }
                comment.setRecipe(aux.getRecipe());
                comment.setComment_time_stamp(aux.getComment_time_stamp());
                commentService.save(comment);
                return new ResponseEntity<>(gson.toJson(Map.of("info", "Comment Updated")), HttpStatus.OK);
        }

        @Operation(summary = "Delete a comment by Id")
        @ApiResponse(responseCode = "200", description = "The comment has been deleted", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentRecipe.class))) })
        @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentRecipe.class))) })
        @ApiResponse(responseCode = "404", description = "The comment was not found", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentRecipe.class))) })
        @DeleteMapping("/{idComment}")
        public ResponseEntity<?> delete(@PathVariable Integer idComment) {
                if (commentService.getById(idComment) == null) {
                        return new ResponseEntity<>(gson.toJson(Map.of("error", "Comment not found")), HttpStatus.NOT_FOUND);
                }
                commentService.delete(idComment);
                return new ResponseEntity<>(gson.toJson(Map.of("info", "Comment Deleted")), HttpStatus.OK);
        }

        @Operation(summary = "Get all coments from a recipe paginated")
        @ApiResponse(responseCode = "200", description = "Comments from a recipe has been found", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentRecipe.class))) })
        @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentRecipe.class))) })
        @ApiResponse(responseCode = "404", description = "No comments found", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentRecipe.class))) })
        @GetMapping("/FromRecipe/{idRecipe}")
        public ResponseEntity<?> getCommentsByRecipe(
                        @PathVariable Integer idRecipe,
                        @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                        @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
                if (userService.getById(idRecipe) == null) {
                        return new ResponseEntity<>(gson.toJson(Map.of("error", "Recipe not found")), HttpStatus.NOT_FOUND);
                }
                page = page * pageSize;
                List<CommentRecipe> comments = commentService.getCommentsByRecipe(idRecipe, pageSize, page);
                return new ResponseEntity<>(comments.stream().map(this::convertToDTO).collect(Collectors.toList()), HttpStatus.OK);
        }

        private CommentRecipeDTO convertToDTO(CommentRecipe comment) {
                return modelMapper.map(comment, CommentRecipeDTO.class);
        }

}