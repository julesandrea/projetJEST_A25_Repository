package projestJest.Variante;

import projestJest.*;

/**
 * Variante classique du jeu JEST.
 * Ne modifie pas les règles de base du compteur de score.
 */
public class VarianteClassique implements Variante {

    
    public void appliquerReglesDeScore(CompteurScore compteur) {
        
    }

    
    public String getNom() {
        return "Règles classiques";
    }
}
