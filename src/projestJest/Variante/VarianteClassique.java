package projestJest.Variante;

import projestJest.*;

/**
 * Implémentation de la variante classique du jeu JEST.
 * Cette variante respecte les règles standard sans modification du calcul des scores.
 */
public class VarianteClassique implements Variante {

    /**
     * Constructeur par défaut.
     */
    public VarianteClassique() {
    }

    @Override
    public void appliquerReglesDeScore(CompteurScore compteur) {
        // Aucune règle spéciale à appliquer pour le classique.
    }

    @Override
    public String getNom() {
        return "Règles classiques";
    }
}
