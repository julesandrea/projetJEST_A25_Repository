package projestJest.Strategie;

import projestJest.*;
import projestJest.Carte.*;
import projestJest.Joueur.*;
import java.util.List;
import java.util.Random;

/**
 * Stratégie défensive pour un bot.
 * Privilégie la prudence, évite les risques (Carreaux, Coeurs risqués) et tente de bloquer les adversaires.
 */
public class StrategieDefensive implements Strategie {

    private Random rand = new Random();


    public boolean choisirVisibleOuCachee(Offre offre) {

        Carte visible = offre.getFaceVisible();
        SuiteCarte couleur = visible.getSuite();
        int valeur = visible.getValeur().getFaceValue();

        if (couleur == SuiteCarte.CARREAU)
            return false; 

        if (couleur == SuiteCarte.COEUR && valeur >= 2)
            return false;

        if (valeur == 4 || valeur == 3)
            return true;

        if (couleur == SuiteCarte.PIQUE || couleur == SuiteCarte.TREFLE) {
            if (valeur <= 2)
                return rand.nextInt(100) < 40; 
        }

        if (visible.getValeur().estAs())
            return false; 

        return false;
    }


    public Joueur choisirOffre(List<Joueur> joueursValides) {

        Joueur meilleur = null;
        int plusDangereux = Integer.MIN_VALUE;

        for (Joueur j : joueursValides) {

            Carte visible = j.getOffre().getFaceVisible();
            int danger = evaluerDanger(visible);

            if (danger > plusDangereux) {
                plusDangereux = danger;
                meilleur = j;
            }
        }

        return meilleur;
    }

    /**
     * Évalue le danger d'une carte : plus la carte est dangereuse, plus le bot veut la neutraliser.
     * @param c La carte à évaluer.
     * @return Score de dangerosité.
     */
    private int evaluerDanger(Carte c) {

        int danger = 0;

        int valeur = c.getValeur().getFaceValue();
        SuiteCarte couleur = c.getSuite();

        danger += valeur;

        if (couleur == SuiteCarte.PIQUE || couleur == SuiteCarte.TREFLE)
            danger += 3; 

        if (couleur == SuiteCarte.CARREAU)
            danger += 1; 

        if (couleur == SuiteCarte.COEUR && valeur >= 2)
            danger += 2;

        if (c.getValeur().estAs())
            danger += 4;

        return danger;
    }
}
