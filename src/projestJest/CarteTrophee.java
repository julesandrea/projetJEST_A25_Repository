package projestJest;

public class CarteTrophee extends Carte {

    private String condition; // ex: "Meilleur 3", "Plus de piques"

    public CarteTrophee(String condition) {
        super(null, null);
        this.condition = condition;
    }

    public String getCondition() {
        return condition;
    }

    @Override
    public void accepter(VisiteurScore visiteur) {
        visiteur.visiter(this);
    }

    @Override
    public String toString() {
        return "Trophée : " + condition;
    }
}