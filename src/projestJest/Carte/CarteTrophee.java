package projestJest.Carte;

import projestJest.*;

/**
 * Représente une carte Trophée.
 */
public class CarteTrophee extends Carte {

    private String condition;

    /**
     * Constructeur de CarteTrophee.
     * @param condition La condition pour remporter ce trophée.
     */
    public CarteTrophee(String condition) {
        super(null, null);
        this.condition = condition;
    }

    /**
     * @return La condition du trophée.
     */
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
