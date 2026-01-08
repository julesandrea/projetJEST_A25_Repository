package projestJest.Strategie;

import projestJest.*;
import projestJest.Carte.*;
import projestJest.Joueur.*;
import java.util.List;
import java.util.Random;

/**
 * Stratégie défensive pour un bot.
 * Ce comportement privilégie la minimisation des risques et l'obstruction des adversaires.
 * Le bot évitera les cartes pénalisantes (Carreaux à forte valeur, Coeurs sans joker)
 * et ciblera les cartes qu'il juge dangereuses chez les adversaires pour les retirer du jeu.
 */
public class StrategieDefensive implements Strategie {

    /**
     * Générateur aléatoire pour introduire une part d'imprévisibilité dans les choix équivalents.
     */
    private Random rand = new Random();

    /**
     * Constructeur par défaut.
     */
    public StrategieDefensive() {
    }

    @Override
    public boolean choisirVisibleOuCachee(Offre offre) {

        Carte visible = offre.getFaceVisible();
        
        if (visible == null) return false; 
        
        SuiteCarte couleur = visible.getSuite();
        int valeur = visible.getValeur() != null ? visible.getValeur().getFaceValue() : 0;

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

        if (visible.getValeur() != null && visible.getValeur().estAs())
            return false; 

        return false;
    }

    @Override
    public Joueur choisirOffre(List<Joueur> joueursValides) {

        Joueur meilleur = null;
        int plusDangereux = Integer.MIN_VALUE;

        for (Joueur j : joueursValides) {
            
            if (j.getOffre() == null || j.getOffre().getFaceVisible() == null) continue;

            Carte visible = j.getOffre().getFaceVisible();
            int danger = evaluerDanger(visible);

            if (danger > plusDangereux) {
                plusDangereux = danger;
                meilleur = j;
            }
        }
        
        if (meilleur == null && !joueursValides.isEmpty()) {
            return joueursValides.get(0);
        }

        return meilleur;
    }

    /**
     * Évalue le niveau de "danger" représenté par une carte.
     * Un score élevé signifie que la carte est perçue comme une menace à neutraliser ou accaparer prudemment.
     * 
     * @param c La carte à évaluer.
     * @return Un score entier représentant la dangerosité.
     */
    private int evaluerDanger(Carte c) {

        int danger = 0;
        
        if (c.getValeur() == null || c.getSuite() == null) return 0;

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
