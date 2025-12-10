package projestJest;

import projestJest.Joueur.JoueurVirtuel;
import projestJest.Strategie.StrategieAleatoire;
import projestJest.Variante.VarianteClassique;
import java.lang.reflect.Field;

public class TestMain {

    public static void main(String[] args) {
        System.out.println(">>> DÉBUT DU TEST AUTOMATISÉ <<<");
        
        // 1. Création de la partie
        Partie partie = new Partie();
        
        // 2. Injection de la Vue de Test via Reflection (pour ne pas modifier Partie.java)
        try {
            Field vueField = Partie.class.getDeclaredField("vue");
            vueField.setAccessible(true);
            vueField.set(partie, new VueTest());
            System.out.println(">>> VueTest injectée avec succès.");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // 3. Configuration de la partie
        partie.setVariante(new VarianteClassique());
        partie.activerExtensions(); // Test avec extensions
        System.out.println(">>> Extensions et Variante configurées.");

        // 4. Ajout de joueurs virtuels (pour ne pas bloquer sur des inputs humains)
        partie.ajouterJoueur(new JoueurVirtuel("Bot1", new StrategieAleatoire()));
        partie.ajouterJoueur(new JoueurVirtuel("Bot2", new StrategieAleatoire()));
        partie.ajouterJoueur(new JoueurVirtuel("Bot3", new StrategieAleatoire())); // Partie à 3 joueurs
        System.out.println(">>> 3 Joueurs virtuels ajoutés.");

        // 5. Lancement
        try {
            partie.demarrer();
            System.out.println(">>> Partie terminée normalement.");
        } catch (Exception e) {
            System.err.println(">>> ERREUR PENDANT L'EXÉCUTION DU JEU");
            try (java.io.PrintWriter pw = new java.io.PrintWriter("error.log")) {
                e.printStackTrace(pw);
            } catch (java.io.IOException io) {
                io.printStackTrace();
            }
        }
        
        System.out.println(">>> FIN DU TEST AUTOMATISÉ <<<");
    }
}
