package projestJest.Carte;

import projestJest.VisiteurScore;

/**
 * Extension "Le Mage". 
 * Carte sans couleur ni valeur standard, offrant un bonus de points et annulant l'effet du Joker.
 */
public class CarteMage extends Carte {

    /**
     * Constructeur de CarteMage.
     */
    public CarteMage() {
        super(null, null);
    }

    @Override
    public void accepter(VisiteurScore visiteur) {
        visiteur.visiter(this);
    }

    @Override
    public String toString() {
        return "Le Mage (Extension)";
    }
}
