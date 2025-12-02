package projestJest;

public class CarteJoker extends Carte {

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
