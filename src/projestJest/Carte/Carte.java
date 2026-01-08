package projestJest.Carte;

import projestJest.*;
import java.io.Serializable;

/**
 * Classe abstraite représentant une carte du jeu Jest.
 * Elle sert de base à toutes les cartes (Suites, Joker, Trophées, Extensions).
 * Implémente Serializable pour permettre la sauvegarde de l'état du jeu.
 */
public abstract class Carte implements Serializable {

    /**
     * La valeur faciale de la carte (ex: AS, DEUX, JOKER...).
     */
    protected ValeurCarte valeur;

    /**
     * La couleur de la carte (ex: Pique, Coeur...), peut être null pour certaines cartes spéciales.
     */
    protected SuiteCarte suite; 

    /**
     * Constructeur de base pour une carte.
     * 
     * @param valeur La valeur faciale de la carte.
     * @param suite La couleur (suite) de la carte.
     */
    public Carte(ValeurCarte valeur, SuiteCarte suite) {
        this.valeur = valeur;
        this.suite = suite;
    }

    /**
     * Retourne la valeur faciale de la carte.
     * 
     * @return La valeur de la carte.
     */
    public ValeurCarte getValeur() {
        return valeur;
    }

    /**
     * Retourne la suite (couleur) de la carte.
     * 
     * @return La suite (couleur) de la carte.
     */
    public SuiteCarte getSuite() {
        return suite;
    }

    /**
     * Méthode abstraite permettant d'accepter un visiteur pour le calcul des scores (Pattern Visitor).
     * Chaque type concret de carte implémentera cette méthode pour rediriger vers la méthode de visite appropriée.
     * 
     * @param visiteur Le visiteur qui effectuera le calcul ou l'analyse de la carte.
     */
    public abstract void accepter(VisiteurScore visiteur);

    @Override
    public String toString() {
        if (suite == null)
            return valeur != null ? valeur.toString() : "Carte Spéciale";
        return valeur + " de " + suite;
    }
}
