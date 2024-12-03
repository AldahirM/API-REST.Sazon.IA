package com.proyecto.SazonIA.controller;


import org.springframework.data.domain.Page;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.proyecto.SazonIA.model.Follower;
import com.proyecto.SazonIA.model.User;
import com.proyecto.SazonIA.service.FollowerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;



@RestController
@RequestMapping("follows")
@Tag(name = "Followers", description = "Operations for managing user following relationships")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE,
        RequestMethod.PUT })
public class FollowerController {
    @Autowired
    private FollowerService service;

    private final Gson gson = new Gson();

    // Seguir a un usuario
    @CrossOrigin(origins = "http://localhost:4200")
    @Operation(summary = "Follow a user")
    @ApiResponse(responseCode = "201", description = "User followed", content = @Content(mediaType = "application/json"))
    @PostMapping("/follow")
    public ResponseEntity<String> followUser(
            @RequestParam("userId") @Min(1) int userId,
            @RequestParam("followedId")@Min(1) int followedId) {
        User follower = service.findUserById(userId);
        User followed = service.findUserById(followedId);

        // Validaciones
        if (follower == null || followed == null) {
            return new ResponseEntity<>(gson.toJson(Map.of("error", "User not found")), HttpStatus.NOT_FOUND);
        }
        if (follower.getUser_id() == followed.getUser_id()) {
            return new ResponseEntity<>(gson.toJson(Map.of("error", "You cannot follow yourself")), HttpStatus.BAD_REQUEST);
        }
        if (service.isFollowing(follower, followed)) {
            return new ResponseEntity<>(gson.toJson(Map.of("error", "You are already following this user")), HttpStatus.CONFLICT);
        }

        service.followUser(follower, followed);
        return new ResponseEntity<>(gson.toJson(Map.of("info", "Successfully followed the user")), HttpStatus.CREATED);
    }

    // Dejar de seguir a un usuario
    @CrossOrigin(origins = "http://localhost:4200")
    @Operation(summary = "Unfollow a user")
    @ApiResponse(responseCode = "200", description = "User unfollowed", content = @Content(mediaType = "application/json"))
    @DeleteMapping("/unfollow")
    public ResponseEntity<String> unfollowUser(
            @RequestParam("userId") @Min(1) int userId,
            @RequestParam("followedId") @Min(1) int followedId) {
        User follower = service.findUserById(userId);
        User followed = service.findUserById(followedId);

        // Validaciones
        if (follower == null || followed == null) {
            return new ResponseEntity<>(gson.toJson(Map.of("error", "User not found")), HttpStatus.NOT_FOUND);
        }
        if (follower.getUser_id() == followed.getUser_id()) {
            return new ResponseEntity<>(gson.toJson(Map.of("error", "You cannot unfollow your own account")), HttpStatus.BAD_REQUEST);
        }
        if (!service.isFollowing(follower, followed)) {
            return new ResponseEntity<>(gson.toJson(Map.of("error", "You are currently not following this user")), HttpStatus.CONFLICT);
        }

        service.unfollowUser(follower, followed);
        return new ResponseEntity<>(gson.toJson(Map.of("info", "You are currently not following this user")), HttpStatus.OK);
    }

    // Obtener los seguidores de un usuario con paginación
    @CrossOrigin(origins = "http://localhost:4200")
@Operation(summary = "Get followers of a user with pagination")
@ApiResponse(responseCode = "200", description = "Followers retrieved", content = {
        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Follower.class))) })
@ApiResponse(responseCode = "404", description = "User not found")
@GetMapping("/followers/{userId}")
public ResponseEntity<?> getFollowers(
        @PathVariable @Min(1) int userId,
        @RequestParam(value = "page", defaultValue = "0", required = false) @Min(0) int page,
        @RequestParam(value = "size", defaultValue = "10", required = false) @Min(1) @Max(20) int pageSize) { 
    User followed = service.findUserById(userId);

    if (followed == null) {
        return new ResponseEntity<>(gson.toJson(Map.of("error", "User not found")), HttpStatus.NOT_FOUND);
    }

    Page<Follower> followers = service.getFollowers(followed, page, pageSize);
    return new ResponseEntity<>(followers, HttpStatus.OK);
}

// Obtener los seguidos de un usuario con paginación
@CrossOrigin(origins = "http://localhost:4200")
@Operation(summary = "Get followings of a user with pagination")
@ApiResponse(responseCode = "200", description = "Following users retrieved", content = {
        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Follower.class))) })
@ApiResponse(responseCode = "404", description = "User not found")
@GetMapping("/followings/{userId}")
public ResponseEntity<?> getFollowing(
        @PathVariable @Min(1) int userId,
        @RequestParam(value = "page", defaultValue = "0", required = false) @Min(0) int page,
        @RequestParam(value = "size", defaultValue = "10", required = false) @Min(1) @Max(20) int pageSize) {
    User follower = service.findUserById(userId);

    if (follower == null) {
        return new ResponseEntity<>(gson.toJson(Map.of("error", "User not found")), HttpStatus.NOT_FOUND);
    }

    Page<Follower> following = service.getFollowing(follower, page, pageSize);
    return new ResponseEntity<>(following, HttpStatus.OK);
}

@CrossOrigin(origins = "http://localhost:4200")
@GetMapping("/isFollowing")
@Operation(summary = "Check if a user is following another user")
@ApiResponse(responseCode = "200", description = "Check result", content = @Content(mediaType = "application/json"))
@ApiResponse(responseCode = "404", description = "User not found")
@ApiResponse(responseCode = "400", description = "Invalid user request")
public ResponseEntity<Map<String, Object>> isFollowing(
        @RequestParam("userId") @Min(1) int userId,
        @RequestParam("followedId") @Min(1) int followedId) {
    User follower = service.findUserById(userId);
    User followed = service.findUserById(followedId);

    // Validaciones
    if (follower == null || followed == null) {
        return new ResponseEntity<>(Map.of("error", "User not found"), HttpStatus.NOT_FOUND);
    }
    if (follower.getUser_id() == followed.getUser_id()) {
        return new ResponseEntity<>(Map.of("error", "You cannot check following status for yourself"), HttpStatus.BAD_REQUEST);
    }

    boolean following = service.isFollowing(follower, followed);
    return new ResponseEntity<>(Map.of("isFollowing", following), HttpStatus.OK);
}




}
