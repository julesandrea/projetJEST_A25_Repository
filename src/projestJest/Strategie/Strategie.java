package projestJest.Strategie;

import projestJest.*;
import projestJest.Joueur.*;
import java.util.List;
import java.io.Serializable;

/**
 * Interface définissant la stratégie d'un joueur virtuel.
 */
public interface Strategie extends Serializable {

    /** 
     * Décide si le joueur prend la carte visible ou cachée d'une offre.
     * @param offre L'offre cible.
     * @return true pour visible, false pour cachée.
     */
    boolean choisirVisibleOuCachee(Offre offre);

    /** 
     * Choisit un joueur cibler parmi les joueurs valides.
     * @param joueursValides Liste des joueurs dont l'offre est complète.
     * @return Le joueur choisi.
     */
    Joueur choisirOffre(List<Joueur> joueursValides);
}
