package projestJest.Carte;

import projestJest.*;
import java.io.Serializable;

/**
 * Classe abstraite représentant une carte du jeu Jest.
 * Implémente Serializable pour permettre la sauvegarde de l'état du jeu.
 */
public abstract class Carte implements Serializable {

    protected ValeurCarte valeur;
    protected SuiteCarte suite; 

    /**
     * Constructeur de Carte.
     * @param valeur La valeur faciale de la carte.
     * @param suite La couleur (suite) de la carte.
     */
    public Carte(ValeurCarte valeur, SuiteCarte suite) {
        this.valeur = valeur;
        this.suite = suite;
    }

    /**
     * @return La valeur de la carte.
     */
    public ValeurCarte getValeur() {
        return valeur;
    }

    /**
     * @return La suite (couleur) de la carte.
     */
    public SuiteCarte getSuite() {
        return suite;
    }

    /**
     * Méthode pour le patron Visitor. Permet de visiter la carte pour le calcul des scores.
     * @param visiteur Le visiteur qui effectuera le calcul.
     */
    public abstract void accepter(VisiteurScore visiteur);

    
    public String toString() {
        if (suite == null)
            return valeur.toString();
        return valeur + " de " + suite;
    }
}
