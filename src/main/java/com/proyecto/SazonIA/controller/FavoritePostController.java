package com.proyecto.SazonIA.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;
import com.proyecto.SazonIA.model.FavoritePost;
import com.proyecto.SazonIA.model.FavoritePostId;
import com.proyecto.SazonIA.model.Post;
import com.proyecto.SazonIA.model.User;
import com.proyecto.SazonIA.service.FavoritePostService;
import com.proyecto.SazonIA.service.PostService;
import com.proyecto.SazonIA.service.UserService;

import org.springframework.data.domain.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/favoritePosts")
@Tag(name = "Favorite Posts", description = "Operations related to favorite posts in Sazón.IA")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE,
                RequestMethod.PUT })
public class FavoritePostController {

        @Autowired
        private FavoritePostService favoritePostService;

        @Autowired
        private UserService userService;

        @Autowired
        private PostService postService;

        private final Gson gson = new Gson();

        @Operation(summary = "Save a post as favorite")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Favorite post saved successfully", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                        @ApiResponse(responseCode = "404", description = "User or post not found", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        })
        @PostMapping("/{userId}")
        public ResponseEntity<String> saveFavoritePost(
                        @PathVariable(value = "userId") @Min(1) Integer userId,
                        @RequestParam String postId) {

                Optional<User> user = userService.getByIdOptional(userId);
                if (!user.isPresent()) {
                        return new ResponseEntity<>(gson.toJson(Map.of("error", "User not found")),
                                        HttpStatus.NOT_FOUND);
                }

                Optional<Post> optionalPost = postService.getPostById(postId);
                if (optionalPost.isEmpty()) {
                        return new ResponseEntity<>(gson.toJson(Map.of("error", "Post not found")),
                                        HttpStatus.NOT_FOUND);
                }

                Optional<Post> existingPost = postService.getPostById(postId);
                if (!existingPost.isPresent()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                        .body(gson.toJson(Map.of("error", "Post not found")));
                }

                /*
                if (favoritePostService.isFavorite(userId, postId)) {
                        return new ResponseEntity<>(gson.toJson(Map.of("error", "Post already marked as favorite")),
                                        HttpStatus.CONFLICT);
                }*/

                FavoritePostId favoritePostId = new FavoritePostId(userId, postId);
                FavoritePost favoritePost = new FavoritePost();
                favoritePost.setId(favoritePostId);

                favoritePostService.saveFavoritePost(favoritePost);

                return new ResponseEntity<>(gson.toJson(Map.of("info", "Post successfully marked as favorite")),
                                HttpStatus.CREATED);
        }

        @Operation(summary = "Get all favorite posts by user ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Favorite posts retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FavoritePost.class))),
                        @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        })
        @GetMapping("/{userId}")
        public ResponseEntity<List<FavoritePost>> getFavoritePostsByUserId(
                        @PathVariable(value = "userId", required = true) @Min(1) Integer userId,
                        @RequestParam(defaultValue = "0", required = true) @Min(0) int page,
                        @RequestParam(defaultValue = "10", required = true) @Min(1) int size) {
                // Obtener las publicaciones favoritas con paginación
                Page<FavoritePost> favoritePostsPage = favoritePostService.getFavoritePostsByUserId(userId, page, size);

                // Devolver solo el contenido de la página
                return ResponseEntity.ok(favoritePostsPage.getContent());
        }

        @Operation(summary = "Remove a favorite post")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Favorite post removed successfully", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Favorite post not found", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        })
        @DeleteMapping("/{userId}")
        public ResponseEntity<String> removeFavoritePost(
                        @PathVariable(value = "userId", required = true) @Min(1) Integer userId,
                        @RequestParam String postId) {

                Optional<User> user = userService.getByIdOptional(userId);
                if (!user.isPresent()) {
                        return new ResponseEntity<>(gson.toJson(Map.of("error", "User not found")),
                                        HttpStatus.NOT_FOUND);
                }

                // Verificar si la relación de favorito existe
                if (!favoritePostService.isFavorite(userId, postId)) {
                        return new ResponseEntity<>(gson.toJson(Map.of("error", "Favorite post not found")),
                                        HttpStatus.NOT_FOUND);
                }

                // Si la relación de favorito existe, eliminamos la publicación favorita
                favoritePostService.removeFavoritePost(userId, postId);

                // Retornar una respuesta exitosa
                return ResponseEntity.ok(gson.toJson(Map.of("info", "Favorite post removed successfully")));
        }

        @Operation(summary = "Get content of a specific favorite post by user ID and post ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Favorite post retrieved successfully", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "404", description = "Post not found", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        })
        @GetMapping("post/{userId}")
        public ResponseEntity<String> getContentFavoritePostByUserAndPostId(
                        @PathVariable(value = "userId", required = true) @Min(1) Integer userId,
                        @RequestParam String postId) {

                Optional<User> user = userService.getByIdOptional(userId);
                if (!user.isPresent()) {
                        return new ResponseEntity<>(gson.toJson(Map.of("error", "User not found")),
                                        HttpStatus.NOT_FOUND);
                }

                Optional<Post> post = Optional.ofNullable(
                                favoritePostService.getContentFavoritePostByUserIdAndPostId(userId, postId));

                return ResponseEntity.ok(gson.toJson(post.get()));
        }
}
