package projestJest;

import projestJest.Carte.Carte;
import projestJest.Joueur.Joueur;
import java.util.List;

/**
 * Interface définissant les méthodes d'interaction avec l'utilisateur (Vue).
 * Permet de découpler la logique du jeu de l'affichage (Console, GUI...).
 */
public interface InterfaceUtilisateur {

    /** Affiche un message générique */
    void afficherMessage(String msg);
    
    /** Affiche les trophées en jeu */
    void afficherTrophees(List<Carte> trophees);
    
    /** Affiche le numéro du tour */
    void afficherTour(int numTour);
    
    /** Affiche les offres de tous les joueurs */
    void afficherOffres(List<Joueur> joueurs);
    
    /** Affiche un message de fin de tour */
    void afficherFinTour(int numTour);
    
    /** Affiche le tableau des scores final et le vainqueur */
    void afficherResultats(List<Joueur> joueurs, Joueur vainqueur, int scoreMax);

    /** Demande un entier entre min et max inclus */
    int demanderChoixInt(String question, int min, int max);
    
    /** Demande une chaine de caractères */
    String demanderChaine(String question);
    
    /**
     * Demande au joueur quelle carte de sa main mettre en visible.
     */
    int demanderChoixOffre(Joueur j, Carte c1, Carte c2);
    
    /**
     * Demande au joueur de choisir entre carte visible (1) ou cachée (2).
     */
    int demanderChoixPrise(Joueur j, Offre o);
    
    /**
     * Demande au joueur de choisir un adversaire parmi la liste.
     */
    Joueur demanderChoixAdversaire(Joueur j, List<Joueur> adversaires);
}
