package com.proyecto.SazonIA.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import org.springframework.http.HttpStatus;
//import org.springframework.web.server.ResponseStatusException;

import com.proyecto.SazonIA.model.Post;
import com.proyecto.SazonIA.model.User;
import com.proyecto.SazonIA.service.PostService;
import com.proyecto.SazonIA.service.UserService;

@RestController
@RequestMapping("/posts")
@Tag(name = "Posts", description = "Operations related to posts in Sazón.IA")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE,
        RequestMethod.PUT })
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    private final Gson gson = new Gson();

    @Operation(summary = "Get random posts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Random posts retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/random")
    public ResponseEntity<List<Post>> getRandomPosts(@RequestParam(value = "count", defaultValue = "5", required = true) @Min(1) @Max(10) int count) {
        List<Post> randomPosts = postService.getRandomPosts(count);
        return ResponseEntity.ok(randomPosts);
    }

    @Operation(summary = "Get posts by user ID with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPostsByUserPaginated(
            @PathVariable (value = "userId", required = true) @Min(1) Integer userId,
            @RequestParam(value = "page", defaultValue = "0", required = true) @Min(0) Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10", required = true) @Min(1) Integer pageSize) {

        Optional<User> user = userService.getByIdOptional(userId);
        if (!user.isPresent()) {
            return new ResponseEntity<>(gson.toJson(Map.of("error", "User not found")), HttpStatus.NOT_FOUND);
        }

        List<Post> posts = postService.getPostsByUser(userId, page, pageSize);
        // Respuesta compleja con Gson
        if (posts.isEmpty()) {
            return new ResponseEntity<>(gson.toJson(Map.of("error", "Post not found")), HttpStatus.NOT_FOUND);
        }

        //Map<String, Object> response = Map.of("status", "success", "data", posts);
        //String jsonResponse = gson.toJson(response);
        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "Get a post by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class))),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable String postId) {
        Optional<Post> post = postService.getPostById(postId);
        if (post.isPresent()) {
            return ResponseEntity.ok(post.get());
        } else {
            return new ResponseEntity<>(gson.toJson(Map.of("error", "Post not found")), HttpStatus.NOT_FOUND);
            // throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
    }

    @Operation(summary = "Create a new post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/{userId}")
    public ResponseEntity<?> createPost(@Valid @PathVariable (value = "userId", required = true) @Min(1) Integer userId,
            @RequestParam String title,
            @RequestParam String content) {

        Optional<User> user = userService.getByIdOptional(userId);
        if (!user.isPresent()) {
            return new ResponseEntity<>(gson.toJson(Map.of("error", "User not found")), HttpStatus.NOT_FOUND);
        }
        // Validar parámetros
        if (title == null || title.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(gson.toJson(Map.of("error", "Title cannot be empty")));
        }

        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(gson.toJson(Map.of("error", "Content cannot be empty")));
        }

        if (title.length() > 100) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(gson.toJson(Map.of("error", "Title cannot exceed 100 characters")));
        }

        if (content.length() > 1000) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(gson.toJson(Map.of("error", "Content cannot exceed 1000 characters")));
        }
        Post createdPost = postService.createPost(userId, title, content);

        Map<String, Object> response = Map.of("status", "success", "data", createdPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(gson.toJson(response));
    }

    @Operation(summary = "Update an existing post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/{postId}")
    public ResponseEntity<String> updatePost(
            @PathVariable String postId,
            @RequestParam String title,
            @RequestParam String content) {

        // Validar parámetros
        if (title == null || title.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(gson.toJson(Map.of("error", "Title cannot be empty")));
        }

        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(gson.toJson(Map.of("error", "Content cannot be empty")));
        }

        if (title.length() > 100) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(gson.toJson(Map.of("error", "Title cannot exceed 100 characters")));
        }

        if (content.length() > 1000) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(gson.toJson(Map.of("error", "Content cannot exceed 1000 characters")));
        }
        Optional<Post> existingPost = postService.getPostById(postId);
        if (!existingPost.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(gson.toJson(Map.of("error", "Post not found")));
        }

        Post updatedPost = postService.updatePost(postId, title, content);

        Map<String, Object> response = Map.of("status", "success", "data", updatedPost);
        return ResponseEntity.ok(gson.toJson(response));
    }

    @Operation(summary = "Delete a post by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Post deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable String postId) {
        Optional<Post> existingPost = postService.getPostById(postId);
        if (!existingPost.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(gson.toJson(Map.of("error", "Post not found")));
        }
        boolean isDeleted = postService.deletePost(postId);
        if (isDeleted) {
            return new ResponseEntity<>(gson.toJson(Map.of("info", "Post deleted successfully")), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(gson.toJson(Map.of("info", "Post could not be deleted")), HttpStatus.NOT_FOUND);
        }
    }

}
