package projestJest;

import projestJest.Carte.Carte;
import projestJest.Joueur.Joueur;
import java.util.List;

/**
 * Vue simplifiée pour les tests automatiques.
 * Ne bloque pas sur les entrées utilisateur et effectue des choix par défaut.
 */
public class VueTest implements InterfaceUtilisateur {

    @Override
    public void afficherMessage(String msg) {
        System.out.println("[TEST] " + msg);
    }

    @Override
    public void afficherTrophees(List<Carte> trophees) {
        System.out.println("[TEST] Trophées : " + trophees);
    }

    @Override
    public void afficherTour(int numTour) {
        System.out.println("[TEST] --- TOUR " + numTour + " ---");
    }

    @Override
    public void afficherOffres(List<Joueur> joueurs) {
        System.out.println("[TEST] Offres affichées.");
    }

    @Override
    public void afficherFinTour(int numTour) {
        System.out.println("[TEST] Fin tour " + numTour);
    }

    @Override
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

    // --- Mocks des entrées ---

    @Override
    public int demanderChoixInt(String question, int min, int max) {
        System.out.println("[TEST-INPUT] Demande Int : " + question);
        
        // Si c'est la sauvegarde (0:Non, 1:Oui) -> on répond 0 (Non) pour continuer
        if (question.contains("sauvegarder")) return 0;
        
        // Choix par défaut : le min
        return min;
    }

    @Override
    public String demanderChaine(String question) {
        return "TestString";
    }

    @Override
    public int demanderChoixOffre(Joueur j, Carte c1, Carte c2) {
        // Toujours choix 1
        return 1;
    }

    @Override
    public int demanderChoixPrise(Joueur j, Offre o) {
        // Toujours choix 1
        return 1;
    }

    @Override
    public Joueur demanderChoixAdversaire(Joueur j, List<Joueur> adversaires) {
        // Toujours le premier
        return adversaires.get(0);
    }
}
