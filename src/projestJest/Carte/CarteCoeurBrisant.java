package projestJest.Carte;

import projestJest.VisiteurScore;
import projestJest.ValeurCarte;
import projestJest.SuiteCarte;

/**
 * Extension "Le Cœur Brisant".
 * Se comporte comme un 6 de Cœur avec des règles de score et de trophée spécifiques.
 */
public class CarteCoeurBrisant extends CarteSuite {

    /**
     * Constructeur de CarteCoeurBrisant.
     * Initialisé comme un 6 de Cœur.
     */
    public CarteCoeurBrisant() {
        super(ValeurCarte.SIX, SuiteCarte.COEUR);
    }

    
    public String toString() {
        return "Le Cœur Brisant (6 de Coeur)";
    }
}
