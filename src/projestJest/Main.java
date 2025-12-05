package projestJest;

import projestJest.Carte.*;
import projestJest.Joueur.*;
import projestJest.Strategie.*;
import projestJest.Variante.*;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Partie partie = new Partie();

        System.out.println("=== JEST - MODE CONSOLE ===");

        /* ================================
                 CHOIX DE LA VARIANTE
           ================================ */
        System.out.println("Choisissez une variante :");
        System.out.println("1 - Règles classiques");
        System.out.println("2 - As valent toujours 5");
        System.out.println("3 - Cœurs jamais négatifs");

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

        /* ================================
                 CHOIX DES JOUEURS
           ================================ */
        System.out.println("\nCombien de joueurs humains ? (0-4)");
        int nbHumains = sc.nextInt();

        System.out.println("Combien de joueurs virtuels ? (0-4)");
        int nbVirtuels = sc.nextInt();

        sc.nextLine(); // vider le buffer

        /* --- Création des joueurs humains --- */
        for (int i = 1; i <= nbHumains; i++) {
            System.out.print("Nom du joueur " + i + " : ");
            String nom = sc.nextLine();
            partie.ajouterJoueur(new JoueurHumain(nom));
        }

        /* --- Création des joueurs virtuels --- */
        for (int i = 1; i <= nbVirtuels; i++) {
            String nomBot = "Bot" + i;
            Strategie strategie = new StrategieAleatoire(); // stratégie par défaut
            partie.ajouterJoueur(new JoueurVirtuel(nomBot, strategie));
            System.out.println("Joueur virtuel ajouté : " + nomBot);
        }

        /* ================================
                  LANCEMENT DU JEU
           ================================ */
        System.out.println("\nDémarrage de la partie...\n");
        partie.demarrer();

        sc.close();
    }
}
