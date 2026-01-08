package projestJest.Variante;

import projestJest.*;

/**
 * Implémentation d'une variante où les cartes de coeur ne rapportent jamais de points négatifs.
 * Dans cette variante, même sans le Joker, avoir des coeurs (mais pas tous) ne pénalise pas le score (valeur plancher à 0).
 */
public class VarianteCoeursJamaisNegatifs implements Variante {

    /**
     * Constructeur par défaut.
     */
    public VarianteCoeursJamaisNegatifs() {
    }

    @Override
    public void appliquerReglesDeScore(CompteurScore compteur) {
        compteur.setCoeursJamaisNegatifs(true);
    }

    @Override
    public String getNom() {
        return "Coeurs jamais négatifs";
    }
}
