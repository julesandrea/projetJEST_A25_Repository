package projestJest;

import projestJest.Carte.*;
import projestJest.Variante.*;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * La classe Jest représente la collection de cartes acquises par un joueur au cours de la partie.
 * Ces cartes constituent le score final du joueur.
 * 
 * Cette classe contient la liste des cartes capturées et offre des méthodes pour ajouter des cartes
 * et calculer le score final en appliquant un visiteur de score configuré selon une variante.
 */
public class Jest implements Serializable {

    /**
     * Liste des cartes collectées dans le Jest.
     */
    private List<Carte> cartes;

    /**
     * Constructeur instanciant un Jest vide.
     * Initialise la liste de cartes.
     */
    public Jest() {
        cartes = new ArrayList<>();
    }

    /** 
     * Ajoute une nouvelle carte à la collection du Jest.
     * 
     * @param c La carte à ajouter. Ne doit pas être null.
     * @throws IllegalArgumentException Si la carte passée en paramètre est nulle.
     */
    public void addCarte(Carte c) {
        if (c == null)
            throw new IllegalArgumentException("Impossible d'ajouter une carte null au Jest.");
        cartes.add(c);
    }

    /** 
     * Retourne une copie défensive de la liste des cartes contenues dans le Jest.
     * Cela permet de consulter les cartes sans risquer de modifier la collection interne directement.
     * 
     * @return Une nouvelle liste contenant les cartes du Jest.
     */
    public List<Carte> getCartes() {
        return new ArrayList<>(cartes);
    }

    /** 
     * Retourne le nombre total de cartes présentes dans le Jest.
     * 
     * @return La taille du Jest.
     */
    public int size() {
        return cartes.size();
    }

    /**
     * Calcule le score total accumulé par les cartes de ce Jest.
     * Le calcul délègue la logique à un objet CompteurScore (Visiteur), dont les règles sont définies
     * par la Variante passée en paramètre.
     * 
     * @param variante La variante de règles à appliquer pour le calcul (ex: Classique, Inversée...).
     * @return Le score entier total.
     * @throws IllegalArgumentException Si la variante fournie est nulle.
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

    @Override
    public String toString() {
        return cartes.toString();
    }
}
