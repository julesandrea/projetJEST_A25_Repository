package projestJest.Variante;

import projestJest.*;
import java.io.Serializable;

/**
 * Interface définissant une variante de règles pour le jeu JEST.
 * Une variante influence principalement la méthode de calcul des scores en configurant
 * le visiteur de score (CompteurScore) avec des paramètres spécifiques avant le comptage.
 */
public interface Variante extends Serializable {

    /** 
     * Applique les règles spécifiques de la variante en configurant le compteur de score.
     * Cette méthode est appelée juste avant que le compteur ne visite les cartes du Jest.
     * 
     * @param compteur Le visiteur de score à configurer (ex: activer le bonus As).
     */
    void appliquerReglesDeScore(CompteurScore compteur);

    /** 
     * Retourne le nom lisible de la variante pour l'affichage.
     * 
     * @return Le nom de la variante.
     */
    String getNom();
}
