package projestJest.Variante;

import projestJest.*;

/**
 * Implémentation d'une variante de règles où les As valent toujours 5 points,
 * indépendamment du fait que le joueur possède d'autres cartes de la même couleur ou non.
 */
public class VarianteAsToujours5 implements Variante {

    /**
     * Constructeur par défaut.
     */
    public VarianteAsToujours5() {
    }

    @Override
    public void appliquerReglesDeScore(CompteurScore compteur) {
        compteur.setAsToujours5(true);
    }

    @Override
    public String getNom() {
        return "As valent toujours 5";
    }
}
