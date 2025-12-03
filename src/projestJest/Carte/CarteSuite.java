package projestJest;

public class CarteSuite extends Carte {

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

