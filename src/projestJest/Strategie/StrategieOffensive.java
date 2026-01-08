package projestJest.Strategie;

import projestJest.*;
import projestJest.Carte.*;
import projestJest.Joueur.*;
import java.util.List;
import java.util.Random;

/**
 * Stratégie offensive pour un bot.
 * Ce comportement vise à maximiser le score personnel en prenant agressivement les cartes à forte valeur
 * (cartes noires, As) et en ignorant les risques potentiels.
 */
public class StrategieOffensive implements Strategie {

    /**
     * Générateur aléatoire pour des décisions non déterministes.
     */
    private Random rand = new Random();

    /**
     * Constructeur par défaut.
     */
    public StrategieOffensive() {
    }

    @Override
    public boolean choisirVisibleOuCachee(Offre offre) {

        Carte visible = offre.getFaceVisible();
        
        if (visible == null) return false;
        
        SuiteCarte couleur = visible.getSuite();
        int valeur = visible.getValeur() != null ? visible.getValeur().getFaceValue() : 0;

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

        if (visible.getValeur() != null && visible.getValeur().estAs())
            return true;

        return rand.nextBoolean();
    }

    @Override
    public Joueur choisirOffre(List<Joueur> joueursValides) {

        Joueur meilleur = null;
        int meilleureValeur = -999;

        for (Joueur j : joueursValides) {
            
            if (j.getOffre() == null || j.getOffre().getFaceVisible() == null) continue;

            Carte visible = j.getOffre().getFaceVisible();
            int scorePotentiel = heuristicValue(visible);

            if (scorePotentiel > meilleureValeur) {
                meilleureValeur = scorePotentiel;
                meilleur = j;
            }
        }
        
        if (meilleur == null && !joueursValides.isEmpty()) {
            return joueursValides.get(0);
        }

        return meilleur;
    }

    /**
     * Calcule une valeur heuristique représentant l'intérêt de la carte pour une stratégie offensive.
     * Plus le score est élevé, plus la carte est désirable.
     * 
     * @param c La carte à évaluer.
     * @return Le score heuristique.
     */
    private int heuristicValue(Carte c) {
        
        if (c.getValeur() == null || c.getSuite() == null) return 0;

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
