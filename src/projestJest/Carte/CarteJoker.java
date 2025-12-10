package projestJest.Carte;

import projestJest.*;

/**
 * Représente la carte Joker.
 */
public class CarteJoker extends Carte {

    /**
     * Constructeur pour le Joker.
     */
    public CarteJoker() {
        super(ValeurCarte.JOKER, null);
    }

    @Override
    public void accepter(VisiteurScore visiteur) {
        visiteur.visiter(this);
    }

    @Override
    public String toString() {
        return "Joker";
    }
}
