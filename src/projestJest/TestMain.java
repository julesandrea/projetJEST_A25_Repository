package projestJest;

import projestJest.Joueur.JoueurVirtuel;
import projestJest.Strategie.StrategieAleatoire;
import projestJest.Variante.VarianteClassique;
import java.lang.reflect.Field;

public class TestMain {

    public static void main(String[] args) {
        System.out.println(">>> DÉBUT DU TEST AUTOMATISÉ <<<");
        
        Partie partie = new Partie();
        
        try {
            Field vueField = Partie.class.getDeclaredField("vue");
            vueField.setAccessible(true);
            vueField.set(partie, new VueTest());
            System.out.println(">>> VueTest injectée avec succès.");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        partie.setVariante(new VarianteClassique());
        partie.activerExtensions(); 
        System.out.println(">>> Extensions et Variante configurées.");

        partie.ajouterJoueur(new JoueurVirtuel("Bot1", new StrategieAleatoire()));
        partie.ajouterJoueur(new JoueurVirtuel("Bot2", new StrategieAleatoire()));
        partie.ajouterJoueur(new JoueurVirtuel("Bot3", new StrategieAleatoire())); 
        System.out.println(">>> 3 Joueurs virtuels ajoutés.");

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
