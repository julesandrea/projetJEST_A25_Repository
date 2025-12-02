package projestJest;

public abstract class Carte {

    protected ValeurCarte valeur;
    protected SuiteCarte suite; // null pour le Joker et les trophées

    public Carte(ValeurCarte valeur, SuiteCarte suite) {
        this.valeur = valeur;
        this.suite = suite;
    }

    public ValeurCarte getValeur() {
        return valeur;
    }

    public SuiteCarte getSuite() {
        return suite;
    }

    /** Visitor Pattern */
    public abstract void accepter(VisiteurScore visiteur);

    @Override
    public String toString() {
        if (suite == null)
            return valeur.toString();
        return valeur + " de " + suite;
    }
}