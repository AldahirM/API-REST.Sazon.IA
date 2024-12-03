package com.proyecto.SazonIA.spoonacularApi;

import java.util.List;

public class ApiWinePairingRecipe {
    
    private List<String> pairedWines;

    private String pairingText;

    public List<String> getPairedWines() {
        return pairedWines;
    }

    public void setPairedWines(List<String> pairedWines) {
        this.pairedWines = pairedWines;
    }

    public String getPairingText() {
        return pairingText;
    }

    public void setPairingText(String pairingText) {
        this.pairingText = pairingText;
    }

    
}
