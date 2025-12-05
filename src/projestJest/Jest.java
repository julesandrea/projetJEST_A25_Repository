package projestJest;

import projestJest.Carte.*;
import java.util.ArrayList;
import java.util.List;

public class Jest {

    private List<Carte> cartes;

    public Jest() {
        cartes = new ArrayList<>();
    }

    /** Ajoute une carte au Jest */
    public void addCarte(Carte c) {
        if (c == null)
            throw new IllegalArgumentException("Impossible d'ajouter une carte null au Jest.");
        cartes.add(c);
    }

    /** Renvoie une copie des cartes pour éviter toute modification externe */
    public List<Carte> getCartes() {
        return new ArrayList<>(cartes);
    }

    /** Nombre de cartes du Jest */
    public int size() {
        return cartes.size();
    }

    /**
     * Calcule le score total du Jest selon la variante choisie.
     * 
     * @param variante la variante de jeu appliquée au calcul du score
     * @return score total du Jest
     */
    public int calculerScore(Variante variante) {

        if (variante == null)
            throw new IllegalArgumentException("Une variante doit être fournie pour calculer le score.");

        // 1. Création du visiteur de score
        CompteurScore compteur = new CompteurScore();

        // 2. La variante configure les règles du compteur
        variante.appliquerReglesDeScore(compteur);

        // 3. Chaque carte "accepte" le visiteur
        for (Carte c : cartes) {
            c.accepter(compteur);
        }

        // 4. Score final
        return compteur.getScoreTotal();
    }

    @Override
    public String toString() {
        return cartes.toString();
    }
}
