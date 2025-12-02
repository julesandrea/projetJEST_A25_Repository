package projestJest;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Partie partie = new Partie();

        System.out.println("=== JEST - MODE CONSOLE ===");
        System.out.println("Combien de joueurs humains ? (0-4)");
        int nbHumains = sc.nextInt();

        System.out.println("Combien de joueurs virtuels ? (0-4)");
        int nbVirtuels = sc.nextInt();

        sc.nextLine(); // vider buffer

        /* --- Création des joueurs humains --- */
        for (int i = 1; i <= nbHumains; i++) {
            System.out.print("Nom du joueur " + i + " : ");
            String nom = sc.nextLine();
            partie.ajouterJoueur(new JoueurHumain(nom));
        }

        /* --- Création des joueurs virtuels --- */
        for (int i = 1; i <= nbVirtuels; i++) {
            String nom = "Bot" + i;
            partie.ajouterJoueur(new JoueurVirtuel(nom, new StrategieAleatoire()));
            System.out.println("Joueur virtuel ajouté : " + nom);
        }

        /* --- Démarrer la partie --- */
        partie.demarrer();

        sc.close();
    }
}
