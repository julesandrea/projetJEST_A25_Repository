package projestJest;

import projestJest.Carte.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;

/**
 * La classe Pioche représente le paquet de cartes utilisé dans le jeu.
 * Elle assure la gestion du cycle de vie des cartes avant leur distribution : création, mélange et tirage.
 * 
 * Cette classe supporte les extensions via l'ajout de cartes spéciales et est sérialisable pour la sauvegarde.
 */
public class Pioche implements Serializable {

    /**
     * Liste ordonnée des cartes présentes dans la pioche.
     */
    private List<Carte> cartes;

    /**
     * Constructeur de la Pioche.
     * Génère l'ensemble des cartes de base (Suites + Joker) et les mélange immédiatement.
     */
    public Pioche() {
        cartes = new ArrayList<>();
        genererCartes();
        melanger();
    }

    /**
     * Instancie les 17 cartes du jeu de base :
     * - 4 suites (Coeur, Carreau, Trèfle, Pique) de 4 valeurs (As, 2, 3, 4).
     * - 1 Joker (Bestiole).
     */
    private void genererCartes() {
        for (SuiteCarte suite : SuiteCarte.values()) {
            for (ValeurCarte valeur : new ValeurCarte[]{ValeurCarte.AS, ValeurCarte.DEUX, ValeurCarte.TROIS, ValeurCarte.QUATRE}) {
                cartes.add(new CarteSuite(valeur, suite));
            }
        }
        cartes.add(new CarteJoker());
    }
    
    /**
     * Intègre les cartes de l'extension au paquet actuel et mélange de nouveau la pioche.
     * Ajoute le Mage et le Coeur Brisant (si implémenté comme tel, ici via des classes spécifiques).
     */
    public void ajouterExtensions() {
        cartes.add(new CarteMage());
        cartes.add(new CarteCoeurBrisant());
        melanger();
    }

    /**
     * Mélange aléatoirement l'ordre des cartes dans la pioche.
     */
    public void melanger() {
        Collections.shuffle(cartes);
    }

    /**
     * Retire et retourne la carte située au sommet de la pioche.
     * 
     * @return La carte piochée, ou null si la pioche est vide.
     */
    public Carte piocher() {
        if (cartes.isEmpty()) return null;
        return cartes.remove(0);
    }

    /**
     * Pioche plusieurs cartes consécutivement, destinées à être utilisées comme trophées.
     * 
     * @param nombre Le nombre de cartes à piocher.
     * @return Une liste contenant les cartes piochées.
     */
    public List<Carte> piocherTrophees(int nombre) {
        List<Carte> troph = new ArrayList<>();
        for (int i = 0; i < nombre; i++) {
            Carte c = piocher();
            if (c != null) troph.add(c);
        }
        return troph;
    }

    /**
     * Retourne le nombre de cartes restant actuellement dans la pioche.
     * 
     * @return La taille de la pioche.
     */
    public int taille() {
        return cartes.size();
    }

    /**
     * Vérifie si la pioche est vide.
     * 
     * @return true si aucune carte n'est disponible, false sinon.
     */
    public boolean estVide() {
        return cartes.isEmpty();
    }
}
