package com.proyecto.SazonIA.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Entity
@Table(name = "Recipe")
@JsonIgnoreProperties({ "user", "comments", "recipe_time_stamp" })
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("recipe_id")
    private Integer recipe_id;

    @NotNull(message = "Recipe name must not be null")
    @NotBlank(message = "Recipe name is mandatory or at least must contain one character")
    @Size(min = 1, max = 100, message = "The content must be between 1 and 100 characters")
    @Column(name = "recipe_name", nullable = false)
    @JsonProperty("recipe_name")
    private String recipe_name;

    @NotNull(message = "Ingredients must not be null")
    @NotBlank(message = "Ingredients are mandatory or at least must contain one character")
    @Size(min = 1, max = 250, message = "The content must be between 1 and 250 characters")
    @Column(name = "ingredients", nullable = false)
    @JsonProperty("ingredients")
    private String ingredients;

    @NotNull(message = "Instructions must not be null")
    @NotBlank(message = "Instructions are mandatory or at least must contain one character")
    @Size(min = 1, max = 500, message = "The content must be between 1 and 500 characters")
    @Column(name = "instructions", nullable = false)
    @JsonProperty("instructions")
    private String instructions;

    @NotNull(message = "Preparation time must not be null")
    @NotBlank(message = "Preparation time is mandatory or at least must contain one character")
    @Size(min = 1, max = 20, message = "The content must be between 1 and 20 characters")
    @Column(name = "preparation_time", nullable = false)
    @JsonProperty("preparation_time")
    private String preparation_time;

    @NotNull(message = "Difficulty must not be null")
    @NotBlank(message = "Difficulty is mandatory or at least must contain one character")
    @Size(min = 1, max = 10, message = "The content must be between 1 and 10 characters")
    @Column(name = "difficulty", nullable = false)
    @JsonProperty("difficulty")
    private String difficulty;

    @NotNull(message = "Recipe time stamp must not be null")
    @Column(name = "recipe_time_stamp", nullable = false)
    @JsonProperty("recipe_time_stamp")
    private String recipe_time_stamp;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "recipe")
    private List<CommentRecipe> comments;

    public Recipe() {
    }

    public Integer getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(Integer recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getPreparation_time() {
        return preparation_time;
    }

    public void setPreparation_time(String preparation_time) {
        this.preparation_time = preparation_time;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getRecipe_time_stamp() {
        return recipe_time_stamp;
    }

    public void setRecipe_time_stamp(String recipe_time_stamp) {
        this.recipe_time_stamp = recipe_time_stamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CommentRecipe> getComments() {
        return comments;
    }

    public void setComments(List<CommentRecipe> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "recipe_id=" + recipe_id +
                ", recipe_name='" + recipe_name + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", instructions='" + instructions + '\'' +
                ", preparation_time='" + preparation_time + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", time_stamp=" + recipe_time_stamp +
                ", user=" + user +
                ", comments=" + comments +
                '}';
    }

}