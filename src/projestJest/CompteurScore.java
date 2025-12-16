package projestJest;

import projestJest.Carte.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Calculateur de score implémentant le pattern Visitor.
 * Parcourt les cartes du Jest pour appliquer les règles de score.
 */
public class CompteurScore implements VisiteurScore {

    private List<CarteSuite> cartesSuite = new ArrayList<>();
    private List<CarteTrophee> trophees = new ArrayList<>();
    private CarteJoker joker = null;

    private int scoreTemp = 0;

    private boolean asToujours5 = false;
    private boolean coeursJamaisNegatifs = false;

    /**
     * Active ou désactive la variante "As valent toujours 5".
     * @param b Vrai pour activer.
     */
    public void setAsToujours5(boolean b) {
        this.asToujours5 = b;
    }

    /**
     * Active ou désactive la variante "Coeurs jamais négatifs".
     * @param b Vrai pour activer.
     */
    public void setCoeursJamaisNegatifs(boolean b) {
        this.coeursJamaisNegatifs = b;
    }


    public void visiter(CarteSuite carte) {
        cartesSuite.add(carte);
    }


    public void visiter(CarteJoker carte) {
        this.joker = carte;
    }


    public void visiter(CarteTrophee carte) {
        trophees.add(carte);
    }

    /**
     * Calcule le score total en appliquant toutes les règles.
     * @return Le score final.
     */
    public int getScoreTotal() {

        scoreTemp = 0;

        appliquerReglesCouleurs();
        appliquerReglesAs();
        appliquerReglesJoker();
        appliquerReglesPairesNoires();
        appliquerReglesTrophees();

        return scoreTemp;
    }

    private void appliquerReglesCouleurs() {

        for (CarteSuite c : cartesSuite) {
            int valeur = c.getValeur().getFaceValue();
            SuiteCarte s = c.getSuite();

            switch (s) {
                case PIQUE:
                case TREFLE:
                    scoreTemp += valeur;
                    break;

                case CARREAU:
                    scoreTemp -= valeur;
                    break;

                case COEUR:
                    break;
            }
        }
    }

    private void appliquerReglesAs() {

        for (SuiteCarte s : SuiteCarte.values()) {

            List<CarteSuite> memesCouleurs = new ArrayList<>();

            for (CarteSuite c : cartesSuite) {
                if (c.getSuite() == s)
                    memesCouleurs.add(c);
            }

            CarteSuite as = null;
            for (CarteSuite c : memesCouleurs) {
                if (c.getValeur().estAs()) {
                    as = c;
                    break;
                }
            }

            if (as == null) continue;

            if (asToujours5) {
                scoreTemp += (5 - as.getValeur().getFaceValue());
                continue;
            }

            if (memesCouleurs.size() == 1) {
                scoreTemp += (5 - 1); 
            }
        }
    }

    private boolean magePresent = false;


    public void visiter(CarteMage carte) {
        this.magePresent = true;
        this.scoreTemp += 2; 
    }


    public void visiter(CarteExtension carte) {
        
    }

    private void appliquerReglesJoker() {

        if (joker != null && magePresent) {
             
            int sommeCoeurs = 0;
            for (CarteSuite c : cartesSuite) {
                if (c.getSuite() == SuiteCarte.COEUR) {
                    sommeCoeurs += c.getValeur().getFaceValue();
                }
            }
             
             if (coeursJamaisNegatifs) {
                 scoreTemp += sommeCoeurs; 
             }
             return; 
        }

        int nbCoeurs = 0;
        int sommeCoeurs = 0;

        for (CarteSuite c : cartesSuite) {
            if (c.getSuite() == SuiteCarte.COEUR) {
                nbCoeurs++;
                sommeCoeurs += c.getValeur().getFaceValue();
            }
        }

        if (joker == null) {

            if (coeursJamaisNegatifs) {
                scoreTemp += sommeCoeurs; 
            }
            return;
        }

        if (nbCoeurs == 0) {
            scoreTemp += 4;
            return;
        }

        if (coeursJamaisNegatifs) {
            scoreTemp += sommeCoeurs;
            return;
        }

        if (nbCoeurs < 4) {
            scoreTemp -= sommeCoeurs;
        } else {
            scoreTemp += sommeCoeurs;
        }
    }

    private void appliquerReglesPairesNoires() {

        for (ValeurCarte v : new ValeurCarte[]{ValeurCarte.AS, ValeurCarte.DEUX, ValeurCarte.TROIS, ValeurCarte.QUATRE}) {

            boolean aPique = false;
            boolean aTrefle = false;

            for (CarteSuite c : cartesSuite) {
                if (c.getValeur() == v) {
                    if (c.getSuite() == SuiteCarte.PIQUE) aPique = true;
                    if (c.getSuite() == SuiteCarte.TREFLE) aTrefle = true;
                }
            }

            if (aPique && aTrefle) {
                scoreTemp += 2;
            }
        }
    }
    
    private void appliquerReglesTrophees() {
        
        boolean coeurBrisantPresent = false;
        
        for (CarteSuite c : cartesSuite) {
            if (c.getValeur() == ValeurCarte.SIX) {
                coeurBrisantPresent = true;
                break;
            }
        }
        
        if (coeurBrisantPresent) {
            int nbAs = 0;
            for (CarteSuite c : cartesSuite) {
                if (c.getValeur().estAs()) nbAs++;
            }
            
            if (nbAs < 4) {
                scoreTemp -= 6;
            } else {
                
            }
        }
    }
}
