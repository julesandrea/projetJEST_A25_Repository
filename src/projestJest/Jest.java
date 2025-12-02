package projestJest;

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

    /** Renvoie une copie des cartes pour éviter les modifications externes */
    public List<Carte> getCartes() {
        return new ArrayList<>(cartes);
    }

    /** Nombre de cartes */
    public int size() {
        return cartes.size();
    }

    /** 
     * Calcule le score total du Jest en appliquant le Visitor Score.
     * Le Visitor encapsule TOUTES les règles du jeu Jest.
     */
    public int calculerScore() {
        CompteurScore visiteur = new CompteurScore();
        for (Carte c : cartes) {
            c.accepter(visiteur);
        }
        return visiteur.getScoreTotal();
    }

    @Override
    public String toString() {
        return cartes.toString();
    }
}
