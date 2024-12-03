package com.proyecto.SazonIA.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyecto.SazonIA.model.CommentPost;
//import com.proyecto.SazonIA.model.Post;
import com.proyecto.SazonIA.model.RatingCommentPost;
import com.proyecto.SazonIA.model.User;
import com.proyecto.SazonIA.service.CommentPostService;
import com.proyecto.SazonIA.service.RatingCommentPostService;
import com.proyecto.SazonIA.service.UserService;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

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
@RequestMapping("/ratings/comments")
@Tag(name = "Rating Comments", description = "Operations related to ratings of comments in Sazón.IA")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE,
        RequestMethod.PUT })
public class RatingCommentPostController {

    @Autowired
    private RatingCommentPostService ratingCommentService;

    @Autowired
    private CommentPostService commentService;

    @Autowired
    private UserService userService;

    private final Gson gson = new Gson();

    @Operation(summary = "Create a new rating for a comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rating created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RatingCommentPost.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping
    public ResponseEntity<String> createRatingComment(
            @RequestParam String commentId,
            @RequestParam(value = "userId", defaultValue = "1") @Min(1) Integer userId,
            @RequestParam(value = "value", defaultValue = "5") @Min(1) @Max(5) Integer value) {

        Optional<User> user = userService.getByIdOptional(userId);
        if (!user.isPresent()) {
            return new ResponseEntity<>(gson.toJson(Map.of("error", "User not found")), HttpStatus.NOT_FOUND);
        }

        Optional<CommentPost> existingComment = commentService.getCommentById(commentId);
        if (!existingComment.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(gson.toJson(Map.of("error", "Comment not found")));
        }

        if (ratingCommentService.validateExistingRating(commentId, userId)) {
            return new ResponseEntity<>(gson.toJson(Map.of("error", "User has already rated this post")),
                    HttpStatus.NOT_FOUND);
        }

        RatingCommentPost createdRating = ratingCommentService.createRatingComment(commentId, userId, value);

        Map<String, Object> response = Map.of("status", "success", "data", createdRating);
        String jsonResponse = gson.toJson(response);
        return ResponseEntity.status(HttpStatus.CREATED).body(jsonResponse);
    }

    @Operation(summary = "Update an existing rating for a comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rating updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RatingCommentPost.class))),
            @ApiResponse(responseCode = "404", description = "Rating not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/{ratingId}")
    public ResponseEntity<String> updateRatingComment(
            @PathVariable String ratingId,
            @RequestParam String commentId,
            @RequestParam(value = "userId", defaultValue = "1") @Min(1) Integer userId,
            @RequestParam(value = "value", defaultValue = "5") @Min(1) @Max(5) Integer value) {

        // Verificar si el usuario existe
        Optional<User> user = userService.getByIdOptional(userId);
        if (!user.isPresent()) {
            return new ResponseEntity<>(gson.toJson(Map.of("error", "User not found")), HttpStatus.NOT_FOUND);
        }

        // Verificar si el comentario existe
        Optional<CommentPost> existingComment = commentService.getCommentById(commentId);
        if (!existingComment.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(gson.toJson(Map.of("error", "Comment not found")));
        }

        // Verificar si la calificación existe para el ratingId
        Optional<RatingCommentPost> existingRating = ratingCommentService.getRatingById(ratingId);
        if (!existingRating.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(gson.toJson(Map.of("error", "Rating not found")));
        }

        // Actualizar la calificación
        RatingCommentPost updatedRating = ratingCommentService.updateRatingComment(ratingId, value);
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
    public ResponseEntity<String> deleteRatingComment(@PathVariable String ratingId) {

        Optional<RatingCommentPost> rating = ratingCommentService.getRatingById(ratingId);
        if (!rating.isPresent()) {
            return new ResponseEntity<>(gson.toJson(Map.of("error", "Rating not found")), HttpStatus.NOT_FOUND);
        }

        boolean isDeleted = ratingCommentService.deleteRatingComment(ratingId);
        if (isDeleted) {
            return new ResponseEntity<>(gson.toJson(Map.of("info", "Rating deleted successfully")), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(gson.toJson(Map.of("info", "Rating could not be deleted")),
                    HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get a rating by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rating found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RatingCommentPost.class))),
            @ApiResponse(responseCode = "404", description = "Rating not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{ratingId}")
    public ResponseEntity<String> getRatingById(@PathVariable String ratingId) {
        Optional<RatingCommentPost> rating = ratingCommentService.getRatingById(ratingId);
        if (rating.isPresent()) {
            Map<String, Object> response = Map.of("status", "success", "data", rating.get());
            String jsonResponse = gson.toJson(response);
            return ResponseEntity.ok(jsonResponse);
        } else {
            return new ResponseEntity<>(gson.toJson(Map.of("error", "Rating not found")), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get a rating by comment ID and user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rating retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RatingCommentPost.class))),
            @ApiResponse(responseCode = "404", description = "Rating not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<String> getRatingByCommentIdAndUserId(
            @RequestParam String commentId,
            @RequestParam Integer userId) {
        Optional<RatingCommentPost> rating = ratingCommentService.findRatingByCommentIdAndUserId(commentId, userId);
        if (rating.isPresent()) {
            Map<String, Object> response = Map.of("status", "success", "data", rating.get());
            return ResponseEntity.ok(gson.toJson(response));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(gson.toJson(Map.of("error", "Rating not found")));
        }
    }

}
