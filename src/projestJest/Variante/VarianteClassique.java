package projestJest.Variante;

import projestJest.*;

/**
 * Variante classique du jeu JEST.
 * Ne modifie pas les règles de base du compteur de score.
 */
public class VarianteClassique implements Variante {

    @Override
    public void appliquerReglesDeScore(CompteurScore compteur) {
        // Aucune règle spéciale à activer
    }

    @Override
    public String getNom() {
        return "Règles classiques";
    }
}
