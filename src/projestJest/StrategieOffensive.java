package projestJest;

import java.util.List;
import java.util.Random;

public class StrategieOffensive implements Strategie {

    private Random rand = new Random();

    @Override
    public boolean choisirVisibleOuCachee(Offre offre) {

        Carte visible = offre.getFaceVisible();
        SuiteCarte couleur = visible.getSuite();
        int valeur = visible.getValeur().getFaceValue();

        // === 1. Prendre les grosses valeurs ===
        if (valeur == 4 || valeur == 3) {
            if (couleur == SuiteCarte.PIQUE || couleur == SuiteCarte.TREFLE)
                return true;  // grosse carte noire = jackpot
        }

        // === 2. Éviter les carreaux (négatifs) ===
        if (couleur == SuiteCarte.CARREAU)
            return false;

        // === 3. Éviter les cœurs (sauf Joker dans le Jest) ===
        // → Ici on ne sait pas si le bot a le Joker, donc prudence
        if (couleur == SuiteCarte.COEUR && valeur >= 2)
            return false;

        // === 4. Bonus : valeur 2 ou 3 noire → intéressant ===
        if (couleur == SuiteCarte.PIQUE || couleur == SuiteCarte.TREFLE) {
            if (valeur == 2 || valeur == 3)
                return true;
        }

        // === 5. As visible : souvent bon à prendre ===
        if (visible.getValeur().estAs())
            return true;

        // === 6. Sinon, choix semi-aléatoire mais orienté visible ===
        return rand.nextBoolean();
    }

    @Override
    public Joueur choisirOffre(List<Joueur> joueursValides) {

        Joueur meilleur = null;
        int meilleureValeur = -999;

        for (Joueur j : joueursValides) {

            Carte visible = j.getOffre().getFaceVisible();
            int scorePotentiel = heuristicValue(visible);

            if (scorePotentiel > meilleureValeur) {
                meilleureValeur = scorePotentiel;
                meilleur = j;
            }
        }

        return meilleur;
    }

    /**
     * Heuristique utilisée pour choisir la meilleure carte visible
     */
    private int heuristicValue(Carte c) {

        int base = c.getValeur().getFaceValue();
        SuiteCarte couleur = c.getSuite();

        // Bonus pour couleurs positives
        if (couleur == SuiteCarte.PIQUE || couleur == SuiteCarte.TREFLE)
            base += 2;

        // Malus carreau
        if (couleur == SuiteCarte.CARREAU)
            base -= 3;

        // Cœur malus léger
        if (couleur == SuiteCarte.COEUR)
            base -= 1;

        // Bonus pour AS (potentiellement 5)
        if (c.getValeur().estAs())
            base += 4;

        return base;
    }
}