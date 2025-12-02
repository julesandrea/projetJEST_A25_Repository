package projestJest;

import java.util.List;

public abstract class Joueur {

    protected String nom;
    protected Offre offre;
    protected Jest jest;
    protected Strategie strategie; // null pour un humain

    public Joueur(String nom) {
        this.nom = nom;
        this.jest = new Jest();
    }

    public String getNom() {
        return nom;
    }

    public Offre getOffre() {
        return offre;
    }

    public Jest getJest() {
        return jest;
    }

    public void ajouterAuJest(Carte c) {
        jest.addCarte(c);
    }

    /** Chaque joueur fabrique une offre (visible + cachée) */
    public abstract void faireOffre(Carte c1, Carte c2);

    /** Le joueur choisit visible ou cachée dans l’offre donnée */
    public abstract Carte choisirCarte(Offre offre);

    /** Le joueur choisit CHEZ QUI il veut prendre une carte */
    public abstract Joueur choisirJoueurCible(List<Joueur> joueursValides);

    @Override
    public String toString() {
        return nom + " | Jest : " + jest;
    }
}
