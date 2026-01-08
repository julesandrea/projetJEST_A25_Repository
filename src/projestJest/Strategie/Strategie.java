package projestJest.Strategie;

import projestJest.*;
import projestJest.Joueur.*;
import java.util.List;
import java.io.Serializable;

/**
 * Interface définissant le contrat pour une stratégie de jeu adoptée par un joueur virtuel (Bot).
 * Le pattern Strategy permet de varier le comportement des bots (Agressif, Défensif, Aléatoire)
 * sans modifier la classe JoueurVirtuel.
 */
public interface Strategie extends Serializable {

    /** 
     * Détermine quelle carte le bot doit prendre dans l'offre qu'il a choisie.
     * 
     * @param offre L'offre cible contenant une carte visible et une carte cachée.
     * @return true pour prendre la carte visible, false pour tenter la chance avec la carte cachée.
     */
    boolean choisirVisibleOuCachee(Offre offre);

    /** 
     * Choisit quel joueur cibler pour voler une carte parmi les adversaires disponibles.
     * 
     * @param joueursValides La liste des joueurs possédant une offre valide (non vide).
     * @return Le joueur sélectionné comme cible.
     */
    Joueur choisirOffre(List<Joueur> joueursValides);
}
