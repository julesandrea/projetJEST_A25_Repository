package projestJest;

import projestJest.Carte.Carte;
import projestJest.Joueur.Joueur;
import java.util.List;

/**
 * Interface définissant le contrat pour toute vue ou contrôleur interagissant avec l'utilisateur.
 * Elle abstrait les détails de l'affichage (console, graphique) et permet au modèle de solliciter
 * des entrées utilisateur de manière agnostique.
 */
public interface InterfaceUtilisateur {

    /**
     * Affiche un message générique à l'utilisateur.
     * 
     * @param msg Le message à afficher.
     */
    void afficherMessage(String msg);
    
    /**
     * Affiche la liste des trophées actuellement en jeu.
     * 
     * @param trophees La liste des cartes trophées.
     */
    void afficherTrophees(List<Carte> trophees);
    
    /**
     * Notifie et affiche le début d'un nouveau tour de jeu.
     * 
     * @param numTour Le numéro du tour.
     */
    void afficherTour(int numTour);
    
    /**
     * Affiche l'état des offres de tous les joueurs (cartes visibles et cachées).
     * 
     * @param joueurs La liste des joueurs.
     */
    void afficherOffres(List<Joueur> joueurs);
    
    /**
     * Notifie et affiche la fin du tour courant.
     * 
     * @param numTour Le numéro du tour terminé.
     */
    void afficherFinTour(int numTour);
    
    /**
     * Affiche les résultats finaux de la partie, incluant les scores et le vainqueur.
     * 
     * @param joueurs La liste des joueurs avec leurs scores.
     * @param vainqueur Le joueur déclaré vainqueur.
     * @param scoreMax Le score maximal atteint.
     */
    void afficherResultats(List<Joueur> joueurs, Joueur vainqueur, int scoreMax);

    /**
     * Demande à l'utilisateur de saisir un nombre entier compris dans un intervalle donné.
     * 
     * @param question Le message d'invite.
     * @param min La valeur minimale acceptée.
     * @param max La valeur maximale acceptée.
     * @return L'entier saisi par l'utilisateur.
     */
    int demanderChoixInt(String question, int min, int max);
    
    /**
     * Demande à l'utilisateur de saisir une chaîne de caractères.
     * 
     * @param question Le message d'invite.
     * @return La chaîne saisie par l'utilisateur.
     */
    String demanderChaine(String question);
    
    /**
     * Demande à un joueur spécifique de choisir parmi deux cartes laquelle sera exposée visiblement.
     * 
     * @param j Le joueur concerné.
     * @param c1 La première carte disponible.
     * @param c2 La seconde carte disponible.
     * @return 1 pour choisir c1, 2 pour choisir c2.
     */
    int demanderChoixOffre(Joueur j, Carte c1, Carte c2);
    
    /**
     * Demande à un joueur de choisir entre prendre la carte visible ou la carte cachée d'une offre.
     * 
     * @param j Le joueur effectuant la prise.
     * @param o L'offre cible.
     * @return 1 pour la carte visible, 2 pour la carte cachée.
     */
    int demanderChoixPrise(Joueur j, Offre o);
    
    /**
     * Demande à un joueur de sélectionner un adversaire parmi une liste de cibles potentielles.
     * 
     * @param j Le joueur effectuant le choix.
     * @param adversaires La liste des adversaires disponibles.
     * @return Le joueur sélectionné comme cible.
     */
    Joueur demanderChoixAdversaire(Joueur j, List<Joueur> adversaires);
}
