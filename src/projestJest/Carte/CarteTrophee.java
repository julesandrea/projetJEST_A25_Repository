package projestJest.Carte;

import projestJest.*;

/**
 * Représente une carte Trophée.
 * Les trophées sont des objectifs communs que les joueurs tentent de remplir pour gagner des points supplémentaires.
 */
public class CarteTrophee extends Carte {

    /**
     * Description de la condition nécessaire pour remporter ce trophée.
     */
    private String condition;

    /**
     * Constructeur d'une carte Trophée.
     * 
     * @param condition La condition textuelle pour remporter ce trophée.
     */
    public CarteTrophee(String condition) {
        super(null, null);
        this.condition = condition;
    }

    /**
     * Retourne la condition de victoire du trophée.
     * 
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
