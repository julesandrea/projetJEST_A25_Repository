package projestJest;

/**
 * Classe principale de l'application (MVC).
 */
public class Main {

    public static void main(String[] args) {
        // Initialisation MVC
        Partie partie = new Partie();
        Controleur ctrl = new Controleur(partie);
        partie.setVue(ctrl); // Lier la vue (Controleur) à la partie

        // Configuration de la partie via le GUI/Console
        ctrl.configurerPartie();
        
        // Démarrage du jeu
        partie.demarrer();
    }
}
