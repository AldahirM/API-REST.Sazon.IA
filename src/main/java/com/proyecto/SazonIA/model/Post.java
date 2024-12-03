package com.proyecto.SazonIA.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Document(collection = "Posts")
public class Post {

    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String postId;

    @NotNull(message = "User ID must not be null")
    @Positive(message = "User ID must be a positive number")
    private Integer userId; 

    @NotBlank(message = "Title must not be blank")
    @Size(min = 1, max = 100, message = "Title cannot exceed 100 characters")
    //@Pattern(regexp = "^[a-zA-Z0-9\\s¿?]+$", message = "Title can only contain alphanumeric characters, spaces, and certain punctuation marks")
    private String title;
    
    @NotBlank(message = "Content must not be blank")
    @Size(min = 1, max = 1000, message = "Content cannot exceed 1000 characters or be empty")
    //@Pattern(regexp = "^[\\p{L}\\p{N}\\p{P}\\p{Z}\\n&&[^<>]]+$", message = "Content contains invalid characters")
    private String content;    
    

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String postDate;

    //private List<String> mediaUrls;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String userFullName; // Nombre completo del usuario


    private int ratingSum = 0;  // Suma total de las calificaciones
    private int ratingCount = 0; // Contador de calificaciones

    // Método para obtener el promedio
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public double getRatingAverage() {
        return ratingCount > 0 ? (double) ratingSum / ratingCount : 0.0;
    }

    // Métodos de modificación del ratingSum y ratingCount
    public void addRating(int value) {
        ratingSum += value;
        ratingCount++;
    }

    public void updateRating(int oldValue, int newValue) {
        ratingSum = ratingSum - oldValue + newValue;
    }

    public void removeRating(int value) {
        ratingSum -= value;
        ratingCount--;
    }

    // Constructor por defecto
    public Post() {
        //this.mediaUrls = new ArrayList<>();
        this.postDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // Constructor con parámetros
    public Post(Integer userId, String title, String content) { // Cambiado a Long para userId
        this(); 
        this.userId = userId; // Establecer el id del User (referencia a MySQL)
        this.title = title;
        this.content = content;
        this.postId = UUID.randomUUID().toString(); 
    }

    // Getters y Setters
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    /*public List<String> getMediaUrls() {
        return mediaUrls;
    }

    public void setMediaUrls(List<String> mediaUrls) {
        this.mediaUrls = mediaUrls;
    }*/

    // Getter y Setter para userFullName
    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

}
