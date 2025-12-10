package projestJest.Carte;

import projestJest.*;

/**
 * Représente une carte standard appartenant à une suite (couleur).
 */
public class CarteSuite extends Carte {

    /**
     * Constructeur de CarteSuite.
     * @param valeur Valeur de la carte.
     * @param suite Couleur de la carte.
     * @throws IllegalArgumentException Si la valeur est JOKER ou la suite est null.
     */
    public CarteSuite(ValeurCarte valeur, SuiteCarte suite) {
        super(valeur, suite);

        if (valeur == ValeurCarte.JOKER)
            throw new IllegalArgumentException("Le Joker n'est pas une carte de suite !");
        if (suite == null)
            throw new IllegalArgumentException("Une carte de suite doit avoir une couleur.");
    }

    @Override
    public void accepter(VisiteurScore visiteur) {
        visiteur.visiter(this);
    }
}
