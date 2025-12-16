package projestJest;

import projestJest.Carte.*;

/**
 * Interface pour le calcul des scores (Pattern Visitor).
 */
public interface VisiteurScore {
    /**
     * Visite une carte suite.
     * @param carte La carte suite.
     */
    void visiter(CarteSuite carte);
    /**
     * Visite une carte joker.
     * @param carte La carte joker.
     */
    void visiter(CarteJoker carte);
    /**
     * Visite une carte trophée.
     * @param carte La carte trophée.
     */
    void visiter(CarteTrophee carte);
    /**
     * Visite une carte mage.
     * @param carte La carte mage.
     */
    void visiter(CarteMage carte);

    /**
     * Visite une carte extension.
     * @param carte La carte extension.
     */
    void visiter(CarteExtension carte);
}
