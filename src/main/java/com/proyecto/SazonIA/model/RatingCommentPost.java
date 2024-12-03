package com.proyecto.SazonIA.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

/**
 * Clase que representa una valoración de un comentario en la base de datos.
 */
@Document(collection = "RatingsComment")
public class RatingCommentPost {

    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String ratingId; // Identificador único del rating

    @NotNull(message = "Comment ID must not be null")
    private String commentId; // ID del comentario asociado al rating

    @NotNull(message = "User ID must not be null")
    @Positive(message = "User ID must be a positive number")
    private Integer userId;  // ID del usuario que realiza la valoración

    @Min(value = 0, message = "Rating must be at least 0")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer value; // Valoración del comentario (0-5)

    // Constructor por defecto
    public RatingCommentPost() {
        this.ratingId = UUID.randomUUID().toString(); // Generar ID aleatorio al crear el objeto
    }

    // Constructor con parámetros
    public RatingCommentPost(String commentId, Integer userId, Integer value) {
        this(); // Llama al constructor por defecto para generar ratingId
        this.commentId = commentId;
        this.userId = userId;
        this.value = value;
    }

    // Getters y Setters
    public String getRatingId() {
        return ratingId;
    }

    public void setRatingId(String ratingId) {
        this.ratingId = ratingId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
