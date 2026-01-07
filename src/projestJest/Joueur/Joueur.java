package projestJest.Joueur;

import projestJest.*;
import projestJest.Carte.*;
import projestJest.Strategie.*;
import java.util.List;

import projestJest.InterfaceUtilisateur;
import java.io.Serializable;

/**
 * Classe abstraite représentant un joueur (humain ou virtuel).
 */
public abstract class Joueur implements Serializable {

    protected String nom;
    protected Offre offre;
    protected Jest jest;
    protected Strategie strategie; 
    protected int score; // Score calculé en fin de partie

    /**
     * Constructeur parent pour tous les joueurs.
     * @param nom Nom du joueur.
     */
    public Joueur(String nom) {
        this.nom = nom;
        this.jest = new Jest();
        this.score = 0;
    }

    /**
     * @return Le nom du joueur.
     */
    public String getNom() {
        return nom;
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * @return L'offre actuelle constituée par le joueur.
     */
    public Offre getOffre() {
        return offre;
    }

    /**
     * @return La pile de cartes récupérées par le joueur (Jest).
     */
    public Jest getJest() {
        return jest;
    }

    /**
     * Ajoute une carte au Jest du joueur.
     * @param c La carte à ajouter.
     */
    public void ajouterAuJest(Carte c) {
        jest.addCarte(c);
    }

    /**
     * Le joueur prépare son offre en choisissant quelle carte montrer.
     * @param c1 Première carte en main.
     * @param c2 Deuxième carte en main.
     * @param vue Interface utilisateur pour interagir.
     */
    public abstract void faireOffre(Carte c1, Carte c2, InterfaceUtilisateur vue);

    /**
     * Le joueur choisit une carte à prendre dans une offre (visible ou cachée).
     * @param offre L'offre cible.
     * @param vue Interface utilisateur.
     * @return La carte choisie et retirée de l'offre.
     */
    public abstract Carte choisirCarte(Offre offre, InterfaceUtilisateur vue);

    /**
     * Le joueur choisit un adversaire cible pour prendre une carte.
     * @param joueursValides Liste des joueurs ayant une offre complète.
     * @param vue Interface utilisateur.
     * @return Le joueur choisi.
     */
    public abstract Joueur choisirJoueurCible(List<Joueur> joueursValides, InterfaceUtilisateur vue);

    
    public String toString() {
        return nom + " | Jest : " + jest;
    }
}
