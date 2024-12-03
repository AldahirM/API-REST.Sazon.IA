package com.proyecto.SazonIA.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyecto.SazonIA.model.Post;
import com.proyecto.SazonIA.model.RatingPost;
import com.proyecto.SazonIA.model.User;
import com.proyecto.SazonIA.service.PostService;
import com.proyecto.SazonIA.service.RatingPostService;
import com.proyecto.SazonIA.service.UserService;

//import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/ratings")
@Tag(name = "Ratings Post", description = "Operations related to ratings of posts in Sazón.IA")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE,
        RequestMethod.PUT })
public class RatingPostController {

    @Autowired
    private RatingPostService ratingService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    private final Gson gson = new Gson();

    @Operation(summary = "Create a new rating")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rating created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RatingPost.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping
    public ResponseEntity<String> createRating(
            @RequestParam String postId,
            @RequestParam(value = "userId", defaultValue = "1") @Min(1) Integer userId,
            @RequestParam(value = "value", defaultValue = "5") @Min(1) @Max(5) Integer value) {

        Optional<User> user = userService.getByIdOptional(userId);
        if (!user.isPresent()) {
            return new ResponseEntity<>(gson.toJson(Map.of("error", "User not found")), HttpStatus.NOT_FOUND);
        }

        Optional<Post> existingPost = postService.getPostById(postId);
        if (!existingPost.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(gson.toJson(Map.of("error", "Post not found")));
        }

        if (ratingService.validateExistingRating(postId, userId)) {
            return new ResponseEntity<>(gson.toJson(Map.of("error", "User has already rated this post")),
                    HttpStatus.NOT_FOUND);
        }

        // Lógica para crear la calificación
        RatingPost createdRating = ratingService.createRatingPost(postId, userId, value);

        // Respuesta de éxito
        Map<String, Object> response = Map.of("status", "success", "data", createdRating);
        String jsonResponse = gson.toJson(response);

        return ResponseEntity.status(HttpStatus.CREATED).body(jsonResponse);
    }

    @Operation(summary = "Update an existing rating")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rating updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RatingPost.class))),
            @ApiResponse(responseCode = "404", description = "Rating not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/{ratingId}")
    public ResponseEntity<String> updateRating(
            @PathVariable String ratingId,
            @RequestParam String postId,
            @RequestParam(value = "userId", defaultValue = "1") @Min(1) Integer userId,
            @RequestParam(value = "value", defaultValue = "5") @Min(1) @Max(5) Integer value) {

        Optional<RatingPost> rating = ratingService.getRatingById(ratingId);
        if (!rating.isPresent()) {
            return new ResponseEntity<>(gson.toJson(Map.of("error", "Rating not found")), HttpStatus.NOT_FOUND);
        }

        // Verificar si el usuario está registrado
        Optional<User> user = userService.getByIdOptional(userId);
        if (!user.isPresent()) {
            return new ResponseEntity<>(gson.toJson(Map.of("error", "User not found")), HttpStatus.NOT_FOUND);
        }

        // Verificar si el post existe
        Optional<Post> existingPost = postService.getPostById(postId);
        if (!existingPost.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(gson.toJson(Map.of("error", "Post not found")));
        }

        // Actualizar la valoración
        RatingPost updatedRating = ratingService.updateRating(ratingId, value);
        // Respuesta de éxito
        Map<String, Object> response = Map.of("status", "success", "data", updatedRating);
        String jsonResponse = gson.toJson(response);
        return ResponseEntity.ok(jsonResponse);
    }

    @Operation(summary = "Delete a rating by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Rating deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Rating not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("/{ratingId}")
    public ResponseEntity<String> deleteRating(@PathVariable String ratingId) {

        Optional<RatingPost> rating = ratingService.getRatingById(ratingId);
        if (!rating.isPresent()) {
            return new ResponseEntity<>(gson.toJson(Map.of("error", "Rating not found")), HttpStatus.NOT_FOUND);
        }

        boolean isDeleted = ratingService.deleteRating(ratingId);

        if (isDeleted) {
            return new ResponseEntity<>(gson.toJson(Map.of("info", "Rating deleted successfully")), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(gson.toJson(Map.of("info", "Rating could not be deleted")),
                    HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get a rating by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rating found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RatingPost.class))),
            @ApiResponse(responseCode = "404", description = "Rating not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{ratingId}")
    public ResponseEntity<String> getRatingById(@PathVariable String ratingId) {
        Optional<RatingPost> rating = ratingService.getRatingById(ratingId);
        if (rating.isPresent()) {
            Map<String, Object> response = Map.of("status", "success", "data", rating.get());
            String jsonResponse = gson.toJson(response);
            return ResponseEntity.ok(jsonResponse);
        } else {
            return new ResponseEntity<>(gson.toJson(Map.of("error", "Rating not found")), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get a rating by postId and userId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rating retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RatingPost.class))),
            @ApiResponse(responseCode = "404", description = "Rating not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<String> getRatingByPostIdAndUserId(
            @PathVariable Integer userId,
            @RequestParam String postId) {

        Optional<RatingPost> rating = ratingService.getRatingByPostIdAndUserId(postId, userId);

        if (rating.isPresent()) {
            Map<String, Object> response = Map.of("status", "success", "data", rating.get());
            return ResponseEntity.ok(gson.toJson(response));
        } else {
            return new ResponseEntity<>(gson.toJson(Map.of("error", "Rating not found")), HttpStatus.NOT_FOUND);
        }
    }

}
