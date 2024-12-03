package com.proyecto.SazonIA.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ResponseStatusException;

import com.proyecto.SazonIA.model.CommentPost;
import com.proyecto.SazonIA.model.Post;
import com.proyecto.SazonIA.model.User;
import com.proyecto.SazonIA.service.CommentPostService;
import com.proyecto.SazonIA.service.PostService;
import com.proyecto.SazonIA.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;

import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/commentsPost")
@Tag(name = "Comments on Posts", description = "Operations related to comments on posts in Sazón.IA")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE,
        RequestMethod.PUT })
public class CommentPostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentPostService commentService;

    @Autowired
    private UserService userService;

    private final Gson gson = new Gson();

    @Operation(summary = "Add a new comment to a specific post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment added successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentPost.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/{postId}")
    public ResponseEntity<?> addComment(@PathVariable String postId,
            @RequestParam(value = "userId", required = true) @Min(1) Integer userId,
            @RequestParam String content) {

        Optional<User> user = userService.getByIdOptional(userId);
        if (!user.isPresent()) {
            return new ResponseEntity<>(gson.toJson(Map.of("error", "User not found")), HttpStatus.NOT_FOUND);
        }

        Optional<Post> post = postService.getPostById(postId);
        if (!post.isPresent()) {
            return new ResponseEntity<>(gson.toJson(Map.of("error", "Post not found")), HttpStatus.NOT_FOUND);
        }

        if (content.length() < 1 || content.length() > 500) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(gson.toJson(Map.of("error", "Content must be between 1 and 500 characters")));
        }

        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(gson.toJson(Map.of("error", "Content cannot be empty")));
        }

        CommentPost comment = commentService.addComment(postId, userId, content);

        // Crear respuesta con Gson
        Map<String, Object> response = Map.of("status", "success", "data", comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(gson.toJson(response));

    }

    @Operation(summary = "Get all comments for a specific post with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentPost.class))),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{postId}")
    public ResponseEntity<?> getCommentsByPostId(
            @PathVariable String postId,
            @RequestParam(value = "page", defaultValue = "0", required = true) @Min(0) Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10", required = true) @Min(1) Integer pageSize) {
        List<CommentPost> comments = commentService.getCommentsByPostId(postId, page, pageSize);
        Optional<Post> post = postService.getPostById(postId);
        if (post.isPresent()) {
            return ResponseEntity.ok(comments);
        } else {
            return new ResponseEntity<>(gson.toJson(Map.of("error", "Post not found")), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Edit a comment by ID and Post ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentPost.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404", description = "Comment or Post not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/{postId}")
    public ResponseEntity<String> updateComment(
            @PathVariable String postId,
            @RequestParam String commentId,
            @RequestParam String content) {

        Optional<Post> existingPost = postService.getPostById(postId);
        if (!existingPost.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(gson.toJson(Map.of("error", "Post not found")));
        }

        Optional<CommentPost> existingComment = commentService.getCommentById(commentId);
        if (!existingComment.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(gson.toJson(Map.of("error", "Comment not found")));
        }

        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(gson.toJson(Map.of("error", "Content cannot be empty")));
        }

        if (content.length() > 500) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(gson.toJson(Map.of("error", "Content cannot exceed 500 characters")));
        }

        CommentPost editedComment = commentService.editComment(postId, commentId, content);

        Map<String, Object> response = Map.of("status", "success", "data", editedComment);
        return ResponseEntity.ok(gson.toJson(response));

    }

    @Operation(summary = "Delete a comment by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comment deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Comment not found or user not authorized", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable String postId,
            @RequestParam String commentId) {

        Optional<Post> existingPost = postService.getPostById(postId);
        if (!existingPost.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(gson.toJson(Map.of("error", "Post not found")));
        }

        Optional<CommentPost> existingComment = commentService.getCommentById(commentId);
        if (!existingComment.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(gson.toJson(Map.of("error", "Comment not found")));
        }

        boolean isDeleted = commentService.deleteComment(postId, commentId);

        if (isDeleted) {
            return new ResponseEntity<>(gson.toJson(Map.of("info", "Comment deleted successfully")), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(gson.toJson(Map.of("info", "Comment could not be deleted")), HttpStatus.NOT_FOUND);
        }
    }
}
