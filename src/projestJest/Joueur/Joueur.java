package projestJest.Joueur;

import projestJest.*;
import projestJest.Carte.*;
import projestJest.Strategie.*;
import java.util.List;

import projestJest.InterfaceUtilisateur;
import java.io.Serializable;

/**
 * Classe abstraite définissant les attributs et comportements communs à tous les joueurs (humains ou virtuels).
 * Elle gère le nom, le score, le Jest (main de cartes collectées) et l'offre courante du joueur.
 */
public abstract class Joueur implements Serializable {

    /**
     * Le nom du joueur.
     */
    protected String nom;

    /**
     * L'offre actuelle du joueur pour le tour en cours.
     */
    protected Offre offre;

    /**
     * Le Jest (collection de cartes) du joueur.
     */
    protected Jest jest;

    /**
     * La stratégie de jeu (utilisée principalement par les joueurs virtuels, null pour les humains).
     */
    protected Strategie strategie; 

    /**
     * Le score final du joueur, calculé en fin de partie.
     */
    protected int score; 

    /**
     * Constructeur parent pour initialiser un joueur.
     * 
     * @param nom Le nom du joueur.
     */
    public Joueur(String nom) {
        this.nom = nom;
        this.jest = new Jest();
        this.score = 0;
    }

    /**
     * Retourne le nom du joueur.
     * 
     * @return Le nom du joueur.
     */
    public String getNom() {
        return nom;
    }
    
    /**
     * Retourne le score final calculé.
     * 
     * @return Le score du joueur.
     */
    public int getScore() {
        return score;
    }
    
    /**
     * Définit le score final du joueur.
     * 
     * @param score Le score à attribuer.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Retourne l'offre courante posée par le joueur.
     * 
     * @return L'offre actuelle constituée par le joueur.
     */
    public Offre getOffre() {
        return offre;
    }

    /**
     * Retourne le Jest du joueur contenant toutes les cartes capturées.
     * 
     * @return La pile de cartes récupérées par le joueur (Jest).
     */
    public Jest getJest() {
        return jest;
    }

    /**
     * Ajoute une carte capturée au Jest du joueur.
     * 
     * @param c La carte à ajouter.
     */
    public void ajouterAuJest(Carte c) {
        jest.addCarte(c);
    }

    /**
     * Méthode abstraite définissant comment un joueur constitue son offre.
     * Il doit choisir parmi deux cartes laquelle sera visible et laquelle sera cachée.
     * 
     * @param c1 La première carte distribuée.
     * @param c2 La deuxième carte distribuée.
     * @param vue L'interface utilisateur pour interagir (si humain).
     */
    public abstract void faireOffre(Carte c1, Carte c2, InterfaceUtilisateur vue);

    /**
     * Méthode abstraite permettant au joueur de choisir une carte à voler dans une offre adverse.
     * 
     * @param offre L'offre cible dans laquelle prendre la carte.
     * @param vue L'interface utilisateur pour interagir.
     * @return La carte choisie et retirée de l'offre.
     */
    public abstract Carte choisirCarte(Offre offre, InterfaceUtilisateur vue);

    /**
     * Méthode abstraite permettant au joueur de choisir quel adversaire cibler pour le vol de carte.
     * 
     * @param joueursValides La liste des joueurs possédant une offre valide.
     * @param vue L'interface utilisateur pour interagir.
     * @return Le joueur choisi comme cible.
     */
    public abstract Joueur choisirJoueurCible(List<Joueur> joueursValides, InterfaceUtilisateur vue);

    @Override
    public String toString() {
        return nom + " | Jest : " + jest;
    }
}
