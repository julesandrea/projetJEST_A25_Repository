package projestJest;

import projestJest.Carte.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;

/**
 * Représente la pioche du jeu. Gère la création, le mélange et la distribution des cartes.
 */
public class Pioche implements Serializable {

    private List<Carte> cartes;

    /**
     * Constructeur de la Pioche. 
     * Initialise les cartes, ajoute le joker et mélange le tout.
     */
    public Pioche() {
        cartes = new ArrayList<>();
        genererCartes();
        melanger();
    }

    /**
     * Génère les 16 cartes de suite et le Joker.
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
     * Ajoute les cartes d'extension à la pioche.
     */
    public void ajouterExtensions() {
        cartes.add(new CarteMage());
        cartes.add(new CarteCoeurBrisant());
        melanger();
    }

    /**
     * Mélange les cartes de la pioche.
     */
    public void melanger() {
        Collections.shuffle(cartes);
    }

    /**
     * Pioche une carte au sommet de la pile.
     * @return La carte piochée, ou null si la pioche est vide.
     */
    public Carte piocher() {
        if (cartes.isEmpty()) return null;
        return cartes.remove(0);
    }

    /**
     * Pioche le nombre spécifié de trophées.
     * @param nombre Le nombre de trophées à piocher.
     * @return Une liste contenant les cartes trophées piochées.
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
     * @return Le nombre de cartes restantes dans la pioche.
     */
    public int taille() {
        return cartes.size();
    }

    /**
     * @return Vrai si la pioche est vide, faux sinon.
     */
    public boolean estVide() {
        return cartes.isEmpty();
    }
}
