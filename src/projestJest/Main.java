package projestJest;

import projestJest.Carte.*;
import projestJest.Joueur.*;
import projestJest.Strategie.*;
import projestJest.Variante.*;

import java.util.Scanner;

/**
 * Classe principale de l'application. 
 * Point d'entrée du programme. 
 */
public class Main {

    /**
     * Point d'entrée du programme.
     * @param args Arguments de la ligne de commande (non utilisés).
     */
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("=== JEST - MODE CONSOLE ===");
        
        System.out.println("1 - Nouvelle Partie");
        System.out.println("2 - Charger une partie");
        System.out.println("3 - Supprimer une sauvegarde");
        System.out.print("Votre choix : ");
        int choixMenu = sc.nextInt();
        sc.nextLine(); 

        Partie partie = null;

        if (choixMenu == 2) {
            partie = Partie.chargerPartie();
            if (partie == null) {
                System.out.println("Échec du chargement. Démarrage d'une nouvelle partie.");
            } else {
                System.out.println("Partie chargée avec succès !");
                partie.demarrer();
                return;
            }
        } else if (choixMenu == 3) {
            java.io.File file = new java.io.File("sauvegarde.ser");
            if (file.delete()) {
                System.out.println("Sauvegarde supprimée avec succès.");
            } else {
                System.out.println("Aucune sauvegarde trouvée ou suppression impossible.");
            }
        }
        
        if (partie == null) {
            partie = new Partie();
        }

        System.out.println("Choisissez une variante :");
        System.out.println("1 - Règles classiques");
        System.out.println("2 - As valent toujours 5");
        System.out.println("3 - Coeurs jamais négatifs");

        System.out.print("Votre choix : ");
        int choixVariante = sc.nextInt();

        switch (choixVariante) {
            case 2:
                partie.setVariante(new VarianteAsToujours5());
                break;
            case 3:
                partie.setVariante(new VarianteCoeursJamaisNegatifs());
                break;
            default:
                partie.setVariante(new VarianteClassique());
                break;
        }
        
        System.out.print("\nInclure les cartes d'extension ? (1:Oui 2:Non) : ");
        int choixExt = 2; 
        try {
             choixExt = sc.nextInt();
        } catch (Exception e) { sc.next(); }
        
        if (choixExt == 1) {
            partie.activerExtensions();
            System.out.println("Extensions activées !");
        }

        int nbHumains = 0;
        int nbVirtuels = 0;
        int nbTotal = 0;
        
        do {
            System.out.println("\nCombien de joueurs humains ? (0-4)");
            try {
                String input = sc.next();
                nbHumains = Integer.parseInt(input);
            } catch(NumberFormatException e) { nbHumains = -1; }

            System.out.println("Combien de joueurs virtuels ? (0-4)");
            try {
                String input = sc.next();
                nbVirtuels = Integer.parseInt(input);
            } catch(NumberFormatException e) { nbVirtuels = -1; }
            
            nbTotal = nbHumains + nbVirtuels;
            
            if (nbTotal < 3 || nbTotal > 4) {
                System.out.println("Le jeu se joue uniquement à 3 ou 4 joueurs ! Réessayez.");
            }
        } while (nbTotal < 3 || nbTotal > 4);

        sc.nextLine(); 

        for (int i = 1; i <= nbHumains; i++) {
            System.out.print("Nom du joueur " + i + " : ");
            String nom = sc.nextLine();
            partie.ajouterJoueur(new JoueurHumain(nom));
        }

        for (int i = 1; i <= nbVirtuels; i++) {
            String nomBot = "Bot" + i;
            System.out.println("Quelle stratégie pour " + nomBot + " ?");
            System.out.println("1 - Aléatoire");
            System.out.println("2 - Offensive");
            System.out.println("3 - Défensive");
            System.out.print("Votre choix : ");
            
            int choixStrat = 1;
            try { choixStrat = sc.nextInt(); } catch(Exception e) { sc.next(); }
            
            Strategie strategie;
            switch(choixStrat) {
                case 2: strategie = new StrategieOffensive(); break;
                case 3: strategie = new StrategieDefensive(); break;
                default: strategie = new StrategieAleatoire(); break;
            }
            
            partie.ajouterJoueur(new JoueurVirtuel(nomBot, strategie));
            System.out.println("Joueur virtuel ajouté : " + nomBot + " (" + strategie.getClass().getSimpleName() + ")");
        }

        System.out.println("\nDémarrage de la partie...\n");
        partie.demarrer();

        sc.close();
    }
}
