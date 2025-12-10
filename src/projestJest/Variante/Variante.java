package projestJest.Variante;

import projestJest.*;
import java.io.Serializable;

/**
 * Interface pour les variantes de règles.
 * Permet de configurer le compteur de score avant le calcul.
 */
public interface Variante extends Serializable {

    /** 
     * Configure le compteur de score selon la variante.
     * @param compteur Le visiteur de score à configurer.
     */
    void appliquerReglesDeScore(CompteurScore compteur);

    /** 
     * @return Le nom de la variante.
     */
    String getNom();
}
