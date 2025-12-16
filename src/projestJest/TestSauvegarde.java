package projestJest;

import projestJest.Joueur.*;
import projestJest.Strategie.*;
import projestJest.Variante.*;
import java.lang.reflect.Field;
import java.util.List;

public class TestSauvegarde {
    public static void main(String[] args) throws Exception {
         System.out.println(">>> DÉBUT TEST SAUVEGARDE <<<");

         Partie p1 = new Partie();
         
         p1.ajouterJoueur(new JoueurVirtuel("BotSave1", new StrategieAleatoire()));
         p1.ajouterJoueur(new JoueurVirtuel("BotSave2", new StrategieOffensive()));
         
         p1.setVariante(new VarianteAsToujours5());
         p1.activerExtensions(); 
         
         Field vueField = Partie.class.getDeclaredField("vue");
         vueField.setAccessible(true);
         vueField.set(p1, new VueTest());

         System.out.println("[P1] État initial configuré : Variante=" + new VarianteAsToujours5().getNom() + ", Joueurs=2");

         System.out.println(">>> Sauvegarde de P1 dans sauvegarde.ser...");
         p1.sauvegarderPartie();
         
         System.out.println(">>> Chargement dans P2...");
         Partie p2 = Partie.chargerPartie();
         
         if (p2 == null) {
             System.err.println("!!! ECHEC: Le chargement a retourné null !!!");
             return;
         }
         
         InterfaceUtilisateur vueP2 = (InterfaceUtilisateur) vueField.get(p2);
         if (vueP2 == null) {
             System.err.println("!!! ECHEC: La vue de P2 est null (initVue non appelé ?) !!!");
         } else {
             System.out.println("[OK] Vue P2 réinitialisée correctement (" + vueP2.getClass().getSimpleName() + ")");
         }

         Field joueursField = Partie.class.getDeclaredField("joueurs");
         joueursField.setAccessible(true);
         List<Joueur> j1 = (List<Joueur>) joueursField.get(p1);
         List<Joueur> j2 = (List<Joueur>) joueursField.get(p2);
         
         if (j1.size() != j2.size()) {
             throw new RuntimeException("Taille joueurs différente : " + j1.size() + " vs " + j2.size());
         }
         if (!j1.get(0).getNom().equals(j2.get(0).getNom())) {
              throw new RuntimeException("Nom Joueur 1 différent");
         }
         if (!j1.get(1).getNom().equals(j2.get(1).getNom())) {
              throw new RuntimeException("Nom Joueur 2 différent");
         }
         System.out.println("[OK] Liste des joueurs conservée.");
         
         Field varianteField = Partie.class.getDeclaredField("variante");
         varianteField.setAccessible(true);
         Variante v1 = (Variante) varianteField.get(p1);
         Variante v2 = (Variante) varianteField.get(p2);
         
         if (!v1.getNom().equals(v2.getNom())) {
             throw new RuntimeException("Variante différente : " + v1.getNom() + " vs " + v2.getNom());
         }
         System.out.println("[OK] Variante conservée (" + v2.getNom() + ").");
         
         Field piocheField = Partie.class.getDeclaredField("pioche");
         piocheField.setAccessible(true);
         Pioche pioche1 = (Pioche) piocheField.get(p1);
         Pioche pioche2 = (Pioche) piocheField.get(p2);
         
         if (pioche1.taille() != pioche2.taille()) {
             throw new RuntimeException("Taille pioche différente : " + pioche1.taille() + " vs " + pioche2.taille());
         }
         System.out.println("[OK] État de la pioche conservé (Taille=" + pioche2.taille() + ").");

         System.out.println(">>> SUCCÈS TOTAL : LE SYSTÈME DE SAUVEGARDE EST FONCTIONNEL <<<");
    }
}
