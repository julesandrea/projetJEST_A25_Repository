package projestJest;

import projestJest.Carte.*;

/**
 * Interface définissant un visiteur pour le calcul des scores (Pattern Visitor).
 * Elle déclare les méthodes de visite pour chaque type concret de carte susceptible d'influencer le score.
 */
public interface VisiteurScore {
    
    /**
     * Traite une carte de suite standard (Coeur, Carreau, Trèfle, Pique).
     * 
     * @param carte La carte suite visitée.
     */
    void visiter(CarteSuite carte);
    
    /**
     * Traite la carte Joker (Bestiole).
     * 
     * @param carte La carte joker visitée.
     */
    void visiter(CarteJoker carte);
    
    /**
     * Traite une carte trophée (si elle fait partie du Jest pour le décompte).
     * 
     * @param carte La carte trophée visitée.
     */
    void visiter(CarteTrophee carte);
    
    /**
     * Traite la carte spéciale Mage.
     * 
     * @param carte La carte mage visitée.
     */
    void visiter(CarteMage carte);

    /**
     * Traite une carte d'extension générique ou future.
     * 
     * @param carte La carte extension visitée.
     */
    void visiter(CarteExtension carte);
}
