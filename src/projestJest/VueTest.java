package projestJest;

import projestJest.Carte.Carte;
import projestJest.Joueur.Joueur;
import java.util.List;

/**
 * Vue simplifiée pour les tests automatiques.
 * Ne bloque pas sur les entrées utilisateur et effectue des choix par défaut.
 */
public class VueTest implements InterfaceUtilisateur {

    /**
     * {@inheritDoc}
     */
    public void afficherMessage(String msg) {
        System.out.println("[TEST] " + msg);
    }

    /**
     * {@inheritDoc}
     */
    public void afficherTrophees(List<Carte> trophees) {
        System.out.println("[TEST] Trophées : " + trophees);
    }

    /**
     * {@inheritDoc}
     */
    public void afficherTour(int numTour) {
        System.out.println("[TEST] --- TOUR " + numTour + " ---");
    }

    /**
     * {@inheritDoc}
     */
    public void afficherOffres(List<Joueur> joueurs) {
        System.out.println("[TEST] Offres affichées.");
    }

    /**
     * {@inheritDoc}
     */
    public void afficherFinTour(int numTour) {
        System.out.println("[TEST] Fin tour " + numTour);
    }

    /**
     * {@inheritDoc}
     */
    public void afficherResultats(List<Joueur> joueurs, Joueur vainqueur, int scoreMax) {
        System.out.println("[TEST] RÉSULTATS FINAUX :");
        for (Joueur j : joueurs) {
            System.out.println("   > " + j.getNom() + " : " + j.getJest().size() + " cartes.");
        }
        if (vainqueur != null) {
            System.out.println("[TEST] Vainqueur : " + vainqueur.getNom() + " (" + scoreMax + " pts)");
        } else {
            System.out.println("[TEST] Pas de vainqueur unique.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public int demanderChoixInt(String question, int min, int max) {
        System.out.println("[TEST-INPUT] Demande Int : " + question);
        
        if (question.contains("sauvegarder")) return 0;
        
        return min;
    }

    /**
     * {@inheritDoc}
     */
    public String demanderChaine(String question) {
        return "TestString";
    }

    /**
     * {@inheritDoc}
     */
    public int demanderChoixOffre(Joueur j, Carte c1, Carte c2) {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    public int demanderChoixPrise(Joueur j, Offre o) {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    public Joueur demanderChoixAdversaire(Joueur j, List<Joueur> adversaires) {
        return adversaires.get(0);
    }
}
