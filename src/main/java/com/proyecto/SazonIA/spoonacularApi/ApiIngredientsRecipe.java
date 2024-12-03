package com.proyecto.SazonIA.spoonacularApi;

import java.util.List;

public class ApiIngredientsRecipe {
    
    private String name;

    private double amount;

    private String unit;

    private String original;

    private ApiMeasuresRecipe measures;

    private String consistency;
    
    private List<String> meta;
    



    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getConsistency() {
        return consistency;
    }
    public void setConsistency(String consistency) {
        this.consistency = consistency;
    }
    public List<String> getMeta() {
        return meta;
    }
    public void setMeta(List<String> meta) {
        this.meta = meta;
    }
    public String getOriginal() {
        return original;
    }
    public void setOriginal(String original) {
        this.original = original;
    }
    public ApiMeasuresRecipe getMeasures() {
        return measures;
    }
    public void setMeasures(ApiMeasuresRecipe measures) {
        this.measures = measures;
    }

    
}
