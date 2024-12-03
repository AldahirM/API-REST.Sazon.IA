package com.proyecto.SazonIA.controller;

import org.modelmapper.ModelMapper;

// import java.util.List;
import java.util.Map;

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
import com.proyecto.SazonIA.DTO.ReplyCommentRecipeDTO;
import com.proyecto.SazonIA.model.CommentRecipe;
import com.proyecto.SazonIA.model.ReplyCommentRecipe;
import com.proyecto.SazonIA.service.CommentRecipeService;
import com.proyecto.SazonIA.service.ReplyCommentRecipeService;
import com.proyecto.SazonIA.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/repliesComments")
@CrossOrigin(origins = "*")
@Tag(name = "Replies from comments", description = "Operations related to Replies in a recipe in Sazón.IA")
public class ReplyCommentRecipeController {
        @Autowired
        private ReplyCommentRecipeService replyCommentService;

        @Autowired
        private CommentRecipeService commentService;

        @Autowired
        private UserService userService;

        @Autowired
        private ModelMapper modelMapper;

        private final Gson gson = new Gson();

        @Operation(summary = "Get a reply by Id")
        @ApiResponse(responseCode = "200", description = "The reply has been found", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentRecipe.class))) })
        @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentRecipe.class))) })
        @ApiResponse(responseCode = "404", description = "No comments registered", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentRecipe.class))) })
        @GetMapping("/{idReply}")
        public ResponseEntity<?> getById(
                        @PathVariable Integer idReply) {
                if (replyCommentService.getById(idReply) == null) {
                        return new ResponseEntity<>(gson.toJson(Map.of("error", "Reply not found")), HttpStatus.NOT_FOUND);

                }
                return new ResponseEntity<>(convertToDto(replyCommentService.getById(idReply)), HttpStatus.OK);
        }

        @Operation(summary = "Save a new reply")
        @ApiResponse(responseCode = "200", description = "The reply has been saved", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentRecipe.class))) })
        @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentRecipe.class))) })
        @PostMapping(params = { "idComment", "idUser" })
        public ResponseEntity<?> save(@RequestBody ReplyCommentRecipe reply,
                        @RequestParam(value = "idComment", required = true) Integer idComment,
                        @RequestParam(value = "idUser", required = true) Integer idUser) {
                if (commentService.getById(idComment) == null) {
                        return new ResponseEntity<>(gson.toJson(Map.of("error", "Comment not found")), HttpStatus.NOT_FOUND);

                }
                reply.setComment(commentService.getById(idComment));
                if (userService.getById(idUser) == null) {
                        return new ResponseEntity<>(gson.toJson(Map.of("error", "User not found")), HttpStatus.NOT_FOUND);
                }
                reply.setUser(userService.getById(idUser));
                reply.setReply_time_stamp(Timestamp.valueOf(LocalDateTime.now()) + "");
                replyCommentService.save(reply);
                return new ResponseEntity<>(HttpStatus.OK);
        }

        @Operation(summary = "Update an existing reply by Id")
        @ApiResponse(responseCode = "200", description = "The reply has been updated", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentRecipe.class))) })
        @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentRecipe.class))) })
        @ApiResponse(responseCode = "404", description = "The reply was not found", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentRecipe.class))) })
        @PutMapping("/{idReply}")
        public ResponseEntity<?> update(@PathVariable Integer idReply,
                        @RequestBody ReplyCommentRecipe reply) {
                ReplyCommentRecipe aux = replyCommentService.getById(idReply);
                if (replyCommentService.getById(idReply) == null) {
                        return new ResponseEntity<>(gson.toJson(Map.of("error", "Reply not found")), HttpStatus.NOT_FOUND);
                }
                reply.setReply_time_stamp(aux.getReply_time_stamp());
                reply.setUser(userService.getById(aux.getUser().getUser_id()));
                reply.setComment(commentService.getById(aux.getComment().getComment_id()));
                replyCommentService.save(reply);
                return new ResponseEntity<>(gson.toJson(Map.of("info", "Reply Updated")), HttpStatus.OK);
        }

        @Operation(summary = "Delete a reply by Id")
        @ApiResponse(responseCode = "200", description = "The reply has been deleted", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentRecipe.class))) })
        @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentRecipe.class))) })
        @ApiResponse(responseCode = "404", description = "The reply was not found", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentRecipe.class))) })
        @DeleteMapping("/{idReply}")
        public ResponseEntity<?> delete(@PathVariable Integer idReply) {
                if (replyCommentService.getById(idReply) == null) {
                        return new ResponseEntity<>(gson.toJson(Map.of("error", "Reply not found")), HttpStatus.NOT_FOUND);
                }
                replyCommentService.delete(idReply);
                return new ResponseEntity<>(gson.toJson(Map.of("info", "Reply deleted")),HttpStatus.OK);
        }

        @Operation(summary = "Get all replies from a comment paginated")
        @ApiResponse(responseCode = "200", description = "The replies have been found", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentRecipe.class))) })
        @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentRecipe.class))) })
        @ApiResponse(responseCode = "404", description = "No replies found", content = {
                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentRecipe.class))) })
        @GetMapping("FromComment/{idComment}")
        public ResponseEntity<?> getrepliesByComment(
                        @PathVariable Integer idComment,
                        @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                        @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
                if (commentService.getById(idComment) == null) {
                        return new ResponseEntity<>(gson.toJson(Map.of("error", "Comment not found")), HttpStatus.NOT_FOUND);
                }
                page = page * pageSize;
                List<ReplyCommentRecipe> replies = replyCommentService.getRepliesByComment(idComment, pageSize, page);
                return new ResponseEntity<>(replies.stream().map(this::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
        }

        private ReplyCommentRecipeDTO convertToDto(ReplyCommentRecipe reply) {
                return modelMapper.map(reply, ReplyCommentRecipeDTO.class);
        }

}
