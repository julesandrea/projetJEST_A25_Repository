package projestJest.Strategie;

import projestJest.*;
import projestJest.Carte.*;
import projestJest.Joueur.*;
import java.util.List;
import java.util.Random;

/**
 * Stratégie offensive pour un bot.
 * Cherche à maximiser le score en prenant les cartes à forte valeur (noires, As).
 */
public class StrategieOffensive implements Strategie {

    private Random rand = new Random();


    public boolean choisirVisibleOuCachee(Offre offre) {

        Carte visible = offre.getFaceVisible();
        SuiteCarte couleur = visible.getSuite();
        int valeur = visible.getValeur().getFaceValue();

        if (valeur == 4 || valeur == 3) {
            if (couleur == SuiteCarte.PIQUE || couleur == SuiteCarte.TREFLE)
                return true;  
        }

        if (couleur == SuiteCarte.CARREAU)
            return false;

        if (couleur == SuiteCarte.COEUR && valeur >= 2)
            return false;

        if (couleur == SuiteCarte.PIQUE || couleur == SuiteCarte.TREFLE) {
            if (valeur == 2 || valeur == 3)
                return true;
        }

        if (visible.getValeur().estAs())
            return true;

        return rand.nextBoolean();
    }


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
     * Heuristique donnant une valeur approximative d'intérêt pour une carte.
     * @param c La carte à évaluer.
     * @return Score heuristique.
     */
    private int heuristicValue(Carte c) {

        int base = c.getValeur().getFaceValue();
        SuiteCarte couleur = c.getSuite();

        if (couleur == SuiteCarte.PIQUE || couleur == SuiteCarte.TREFLE)
            base += 2;

        if (couleur == SuiteCarte.CARREAU)
            base -= 3;

        if (couleur == SuiteCarte.COEUR)
            base -= 1;

        if (c.getValeur().estAs())
            base += 4;

        return base;
    }
}
