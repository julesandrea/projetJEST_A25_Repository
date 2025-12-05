package projestJest.Variante;

import projestJest.*;

public interface Variante {

    /** Appliqué juste avant le calcul du score d’un Jest */
    void appliquerReglesDeScore(CompteurScore compteur);

    /** Nom affiché dans le menu */
    String getNom();
}
