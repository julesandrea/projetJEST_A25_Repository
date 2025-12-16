package projestJest.Variante;

import projestJest.*;

/**
 * Variante où les coeurs ne rapportent jamais de points négatifs.
 * Ils valent soit 0, soit positif (si Joker ou règles spéciales).
 */
public class VarianteCoeursJamaisNegatifs implements Variante {

    
    public void appliquerReglesDeScore(CompteurScore compteur) {
        compteur.setCoeursJamaisNegatifs(true);
    }

    
    public String getNom() {
        return "Coeurs jamais négatifs";
    }
}
