package com.proyecto.SazonIA.spoonacularApi;

import java.util.List;

public class ApiRecipe {
    
    private Integer id;

    private String title;

    private String image;

    private Integer readyInMinutes;

    private Integer cookingMinutes;

    private Integer preparationMinutes;

    private String sourceName;

    private String sourceUrl;

    private String spoonacularSourceUrl;

    private Integer servings;

    private List<ApiIngredientsRecipe> extendedIngredients;

    private String summary;

    private ApiWinePairingRecipe winePairing;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public ApiWinePairingRecipe getWinePairing() {
        return winePairing;
    }

    public void setWinePairing(ApiWinePairingRecipe winePairing) {
        this.winePairing = winePairing;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getSpoonacularSourceUrl() {
        return spoonacularSourceUrl;
    }

    public void setSpoonacularSourceUrl(String spoonacularSourceUrl) {
        this.spoonacularSourceUrl = spoonacularSourceUrl;
    }

    public Integer getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(Integer readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public Integer getCookingMinutes() {
        return cookingMinutes;
    }

    public void setCookingMinutes(Integer cookingMinutes) {
        this.cookingMinutes = cookingMinutes;
    }

    public Integer getPreparationMinutes() {
        return preparationMinutes;
    }

    public void setPreparationMinutes(Integer preparationMinutes) {
        this.preparationMinutes = preparationMinutes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<ApiIngredientsRecipe> getExtendedIngredients() {
        return extendedIngredients;
    }

    public void setExtendedIngredients(List<ApiIngredientsRecipe> extendedIngredients) {
        this.extendedIngredients = extendedIngredients;
    }

    
}








