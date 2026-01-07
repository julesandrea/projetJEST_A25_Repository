package projestJest;

import projestJest.Carte.Carte;
import projestJest.Joueur.Joueur;
import java.util.List;
import java.util.Scanner;
import java.io.Serializable;

/**
 * Implémentation de la vue en console.
 * Gère les affichages texte et la saisie utilisateur via Scanner.
 */
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Implémentation de la vue en console.
 * Gère les affichages texte et la saisie utilisateur via Scanner.
 */
public class VueConsole implements InterfaceUtilisateur, Serializable, PropertyChangeListener {
    
    private transient Scanner scanner;

    /**
     * Constructeur de VueConsole.
     */
    public VueConsole() {
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Initialise le scanner (utile après sérialisation).
     */
    public void initScanner() {
        if (this.scanner == null) {
            this.scanner = new Scanner(System.in);
        }
    }

    
    public void afficherMessage(String msg) {
        System.out.println(msg);
    }

    
    public void afficherTrophees(List<Carte> trophees) {
        System.out.println("\nTrophées : " + trophees);
    }

    
    public void afficherTour(int numTour) {
        System.out.println("\n----------------------------");
        System.out.println("      TOUR " + numTour);
        System.out.println("----------------------------");
    }

    
    public void afficherOffres(List<Joueur> joueurs) {
        System.out.println("\nOffres actuelles :");
        for (Joueur j : joueurs) {
            System.out.println("- " + j.getNom() + " : " + j.getOffre());
        }
    }

    
    public void afficherFinTour(int numTour) {
        System.out.println("\nFin du tour " + numTour + ".");
    }

    
    public void afficherResultats(List<Joueur> joueurs, Joueur vainqueur, int scoreMax) {
        System.out.println("\n=== Scores finaux ===");
        for (Joueur j : joueurs) {
             System.out.println(j.getNom() + " | Jest : " + j.getJest());
        }
        
        if (vainqueur != null) {
            System.out.println("\nLe vainqueur est : " + vainqueur.getNom() + " avec " + scoreMax + " points.");
        } else {
            System.out.println("\nÉgalité ou erreur dans le calcul du vainqueur.");
        }
    }

    
    public int demanderChoixInt(String question, int min, int max) {
        int choix;
        do {
            System.out.print(question + " [" + min + "-" + max + "] : ");
            while (!scanner.hasNextInt()) {
                System.out.print("Veuillez entrer un nombre entier : ");
                scanner.next();
            }
            choix = scanner.nextInt();
        } while (choix < min || choix > max);
        scanner.nextLine(); 
        return choix;
    }

    
    public String demanderChaine(String question) {
        System.out.print(question);
        return scanner.nextLine();
    }

    
    public int demanderChoixOffre(Joueur j, Carte c1, Carte c2) {
        System.out.println("\n" + j.getNom() + ", voici vos cartes : ");
        System.out.println("1 : " + c1);
        System.out.println("2 : " + c2);
        return demanderChoixInt("Choisissez celle à mettre VISIBLE", 1, 2);
    }

    
    public int demanderChoixPrise(Joueur j, Offre o) {
        System.out.println("\n" + j.getNom() + ", offre sélectionnée : " + o);
        System.out.println("1 : Prendre la carte visible");
        System.out.println("2 : Prendre la carte cachée");
        return demanderChoixInt("Votre choix", 1, 2);
    }

    
    public Joueur demanderChoixAdversaire(Joueur j, List<Joueur> adversaires) {
        System.out.println("\n" + j.getNom() + ", choisissez un joueur chez qui prendre une carte :");
        for (int i = 0; i < adversaires.size(); i++) {
            Joueur adv = adversaires.get(i);
            String visible = (adv.getOffre() != null && adv.getOffre().getFaceVisible() != null) 
                           ? adv.getOffre().getFaceVisible().toString() 
                           : "Aucune";
            System.out.println((i + 1) + " : " + adv.getNom() + " (Carte visible : " + visible + ")");
        }
        int choix = demanderChoixInt("Numéro du joueur cible", 1, adversaires.size());
        return adversaires.get(choix - 1);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "message":
                afficherMessage((String) evt.getNewValue());
                break;
            case "trophees":
                afficherTrophees((List<Carte>) evt.getNewValue());
                break;
            case "tour":
                afficherTour((Integer) evt.getNewValue());
                break;
            case "offres":
                afficherOffres((List<Joueur>) evt.getNewValue());
                break;
            case "fin_tour":
                afficherFinTour((Integer) evt.getNewValue());
                break;
            case "resultats":
                afficherResultats((List<Joueur>) evt.getNewValue(), null, 0); 
                break;
        }
    }
}
