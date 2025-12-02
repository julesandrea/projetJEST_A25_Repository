package projestJest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pioche {

    private List<Carte> cartes;

    public Pioche() {
        cartes = new ArrayList<>();
        genererCartes();
        melanger();
    }

    /** Génère les 16 cartes de suite + le Joker */
    private void genererCartes() {
        // 4 couleurs * 4 valeurs = 16 cartes
        for (SuiteCarte suite : SuiteCarte.values()) {
            for (ValeurCarte valeur : new ValeurCarte[]{ValeurCarte.AS, ValeurCarte.DEUX, ValeurCarte.TROIS, ValeurCarte.QUATRE}) {
                cartes.add(new CarteSuite(valeur, suite));
            }
        }

        // Ajouter le Joker
        cartes.add(new CarteJoker());
    }

    /** Mélange toutes les cartes */
    public void melanger() {
        Collections.shuffle(cartes);
    }

    /** Pioche une carte (retourne null si vide) */
    public Carte piocher() {
        if (cartes.isEmpty()) return null;
        return cartes.remove(0);
    }

    /**
     * Pioche les 2 trophées initiaux (ou 1 trophée si 4 joueurs).
     * Ici : on retourne toujours 2 cartes. La classe Partie décidera
     * d'en utiliser une seule si partie à 4 joueurs.
     */
    public List<Carte> piocherTrophees() {
        List<Carte> troph = new ArrayList<>();
        troph.add(piocher());
        troph.add(piocher());
        return troph;
    }

    /** Nombre de cartes restantes dans la pioche */
    public int taille() {
        return cartes.size();
    }

    /** Vérifie si la pioche est vide */
    public boolean estVide() {
        return cartes.isEmpty();
    }
}
