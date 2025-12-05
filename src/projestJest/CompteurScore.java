package projestJest;

import projestJest.Carte.*;
import java.util.ArrayList;
import java.util.List;

public class CompteurScore implements VisiteurScore {

    // Cartes du Jest
    private List<CarteSuite> cartesSuite = new ArrayList<>();
    private List<CarteTrophee> trophees = new ArrayList<>();
    private CarteJoker joker = null;

    private int scoreTemp = 0;

    // Options activées par les variantes
    private boolean asToujours5 = false;
    private boolean coeursJamaisNegatifs = false;

    /* ============================================================
                          MÉTHODES DE CONFIGURATION
       ============================================================ */

    public void setAsToujours5(boolean b) {
        this.asToujours5 = b;
    }

    public void setCoeursJamaisNegatifs(boolean b) {
        this.coeursJamaisNegatifs = b;
    }

    /* ============================================================
                             VISITEURS
       ============================================================ */

    @Override
    public void visiter(CarteSuite carte) {
        cartesSuite.add(carte);
    }

    @Override
    public void visiter(CarteJoker carte) {
        this.joker = carte;
    }

    @Override
    public void visiter(CarteTrophee carte) {
        trophees.add(carte);
    }

    /* ============================================================
                       CALCUL GLOBAL DU SCORE
       ============================================================ */

    public int getScoreTotal() {

        scoreTemp = 0;

        appliquerReglesCouleurs();
        appliquerReglesAs();
        appliquerReglesJoker();
        appliquerReglesPairesNoires();
        appliquerReglesTrophees();

        return scoreTemp;
    }

    /* ============================================================
                       RÈGLE 1 : COULEURS
       ============================================================ */

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
                    // le cas des cœurs dépendra plus tard du Joker
                    break;
            }
        }
    }

    /* ============================================================
                       RÈGLE 2 : AS
       ============================================================ */

    private void appliquerReglesAs() {

        for (SuiteCarte s : SuiteCarte.values()) {

            List<CarteSuite> memesCouleurs = new ArrayList<>();

            for (CarteSuite c : cartesSuite) {
                if (c.getSuite() == s)
                    memesCouleurs.add(c);
            }

            // Cherche un As dans cette couleur
            CarteSuite as = null;
            for (CarteSuite c : memesCouleurs) {
                if (c.getValeur().estAs()) {
                    as = c;
                    break;
                }
            }

            if (as == null) continue;

            // Variante : As valent toujours 5
            if (asToujours5) {
                scoreTemp += (5 - as.getValeur().getFaceValue());
                continue;
            }

            // Règle classique : As vaut 5 seulement s'il est seul dans sa couleur
            if (memesCouleurs.size() == 1) {
                scoreTemp += (5 - 1); // on ajoute +4
            }
        }
    }

    /* ============================================================
                       RÈGLE 3 : JOKER + COEURS
       ============================================================ */

    private void appliquerReglesJoker() {

        // Compter les cœurs
        int nbCoeurs = 0;
        int sommeCoeurs = 0;

        for (CarteSuite c : cartesSuite) {
            if (c.getSuite() == SuiteCarte.COEUR) {
                nbCoeurs++;
                sommeCoeurs += c.getValeur().getFaceValue();
            }
        }

        // Pas de Joker → règle simple
        if (joker == null) {

            if (coeursJamaisNegatifs) {
                scoreTemp += sommeCoeurs; // tous positifs
            }
            // sinon : cœurs valent 0 → règle classique
            return;
        }

        // Joker présent
        if (nbCoeurs == 0) {
            // Joker seul : +4
            scoreTemp += 4;
            return;
        }

        if (coeursJamaisNegatifs) {
            // Variante : les cœurs ne descendent jamais en négatif
            scoreTemp += sommeCoeurs;
            return;
        }

        // RÈGLES CLASSIQUES
        if (nbCoeurs < 4) {
            // Joker + 1 à 3 cœurs : cœurs négatifs
            scoreTemp -= sommeCoeurs;
        } else {
            // Joker + 4 cœurs : cœurs positifs
            scoreTemp += sommeCoeurs;
        }
    }

    /* ============================================================
                       RÈGLE 4 : PAIRES NOIRES
       ============================================================ */

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

    /* ============================================================
                       RÈGLE 5 : TROPHÉES
       ============================================================ */

    private void appliquerReglesTrophees() {
        // Implémentation personnalisable par variante si besoin
        // Actuellement, aucun trophée n'a encore de règle codée
    }
}
