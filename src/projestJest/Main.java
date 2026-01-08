package projestJest;

/**
 * Classe principale point d'entrée de l'application JEST.
 * Elle est responsable de l'initialisation des composants du modèle MVC (Modèle-Vue-Contrôleur)
 * et du lancement de la boucle de jeu.
 */
public class Main {

    /**
     * Constructeur par défaut.
     */
    public Main() {
        super();
    }

    /**
     * Point d'entrée du programme.
     * Instancie la Partie (Modèle) et le Contrôleur.
     * Configure le jeu via le contrôleur puis démarre la logique principale.
     * 
     * @param args Arguments de la ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        
        Partie partie = new Partie();
        Controleur ctrl = new Controleur(partie);
        partie.setVue(ctrl); 

        ctrl.configurerPartie();
        
        partie.demarrer();
    }
}
