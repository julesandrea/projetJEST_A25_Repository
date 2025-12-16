package projestJest;

import projestJest.Carte.*;
import projestJest.Variante.*;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * Représente le "Jest" d'un joueur, c'est-à-dire l'ensemble des cartes collectées
 * et comptabilisées pour le score final.
 */
public class Jest implements Serializable {

    private List<Carte> cartes;

    /**
     * Constructeur d'un Jest vide.
     */
    public Jest() {
        cartes = new ArrayList<>();
    }

    /** 
     * Ajoute une carte au Jest. 
     * @param c La carte à ajouter.
     */
    public void addCarte(Carte c) {
        if (c == null)
            throw new IllegalArgumentException("Impossible d'ajouter une carte null au Jest.");
        cartes.add(c);
    }

    /** 
     * @return Une copie de la liste des cartes du Jest.
     */
    public List<Carte> getCartes() {
        return new ArrayList<>(cartes);
    }

    /** 
     * @return Le nombre de cartes dans le Jest.
     */
    public int size() {
        return cartes.size();
    }

    /**
     * Calcule le score total du Jest selon la variante choisie en utilisant le pattern Visitor.
     * 
     * @param variante la variante de jeu appliquée au calcul du score
     * @return score total du Jest
     */
    public int calculerScore(Variante variante) {

        if (variante == null)
            throw new IllegalArgumentException("Une variante doit être fournie pour calculer le score.");

        CompteurScore compteur = new CompteurScore();

        variante.appliquerReglesDeScore(compteur);

        for (Carte c : cartes) {
            c.accepter(compteur);
        }

        return compteur.getScoreTotal();
    }

    
    public String toString() {
        return cartes.toString();
    }
}
