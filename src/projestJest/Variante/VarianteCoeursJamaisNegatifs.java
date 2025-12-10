package projestJest.Variante;

import projestJest.*;

/**
 * Variante où les cœurs ne rapportent jamais de points négatifs.
 * Ils valent soit 0, soit positif (si Joker ou règles spéciales).
 */
public class VarianteCoeursJamaisNegatifs implements Variante {

    @Override
    public void appliquerReglesDeScore(CompteurScore compteur) {
        compteur.setCoeursJamaisNegatifs(true);
    }

    @Override
    public String getNom() {
        return "Cœurs jamais négatifs";
    }
}
