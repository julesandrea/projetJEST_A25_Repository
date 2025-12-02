package projestJest;

import java.util.ArrayList;
import java.util.List;

public class CompteurScore implements VisiteurScore {

    private List<CarteSuite> cartesSuite = new ArrayList<>();
    private boolean jokerPresent = false;
    private CarteJoker joker = null;
    private List<CarteTrophee> trophees = new ArrayList<>();

    private int scoreTemp = 0;

    /** Visite une carte de couleur (Pique, Trèfle, Carreau, Coeur) */
    @Override
    public void visiter(CarteSuite carte) {
        cartesSuite.add(carte);
    }

    /** Visite le Joker */
    @Override
    public void visiter(CarteJoker carte) {
        jokerPresent = true;
        joker = carte;
    }

    /** Visite un trophée */
    @Override
    public void visiter(CarteTrophee carte) {
        trophees.add(carte);
    }

    /**
     * Retourne le score final total du Jest,
     * après traitement DE TOUTES les règles Jest.
     */
    public int getScoreTotal() {
        scoreTemp = 0;

        appliquerReglesCouleurs();
        appliquerReglesAs();
        appliquerReglesJoker();
        appliquerReglesPaires();
        appliquerReglesTrophees();

        return scoreTemp;
    }

    /** ----------------------
     * RÈGLE 1 : Couleurs
     * ---------------------- */
    private void appliquerReglesCouleurs() {
        for (CarteSuite c : cartesSuite) {

            int valeur = c.getValeur().getFaceValue();
            SuiteCarte couleur = c.getSuite();

            switch (couleur) {
                case PIQUE:
                case TREFLE:
                    scoreTemp += valeur;
                    break;

                case CARREAU:
                    scoreTemp -= valeur;
                    break;

                case COEUR:
                    // les coeurs dépendent du Joker → traités plus tard
                    break;
            }
        }
    }

    /** ----------------------
     * RÈGLE 2 : As
     * ---------------------- */
    private void appliquerReglesAs() {

        // On compte combien d'As il y a par couleur
        for (SuiteCarte couleur : SuiteCarte.values()) {

            int count = 0;
            CarteSuite as = null;

            for (CarteSuite c : cartesSuite) {
                if (c.getSuite() == couleur && c.getValeur().estAs()) {
                    count++;
                    as = c;
                }
            }

            // S'il y a exactement 1 As dans cette couleur → il vaut 5 
            if (count == 1) {
                scoreTemp += (5 - 1);  
                // On ajoute +4 parce qu'on avait déjà ajouté +1 dans les couleurs (ou 0 si coeur)
            }
        }
    }

    /** ----------------------
     * RÈGLE 3 : Joker + Cœurs
     * ---------------------- */
    private void appliquerReglesJoker() {

        int nbCoeurs = 0;
        int sommeCoeurs = 0;

        for (CarteSuite c : cartesSuite) {
            if (c.getSuite() == SuiteCarte.COEUR) {
                nbCoeurs++;
                sommeCoeurs += c.getValeur().getFaceValue();
            }
        }

        if (!jokerPresent) {
            // Pas de Joker → les coeurs ne valent rien
            return;
        }

        // Joker présent
        if (nbCoeurs == 0) {
            // Joker seul → +4
            scoreTemp += 4;
        }
        else if (nbCoeurs < 4) {
            // Joker + 1 à 3 coeurs → les coeurs sont NEGATIFS
            scoreTemp -= sommeCoeurs;
        }
        else {
            // Joker + 4 coeurs → coeurs positifs
            scoreTemp += sommeCoeurs;
        }
    }

    /** ----------------------
     * RÈGLE 4 : Paires noires (Spade + Club de même valeur)
     * ---------------------- */
    private void appliquerReglesPaires() {
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
                scoreTemp += 2; // bonus paire noire
            }
        }
    }

    /** ----------------------
     * RÈGLE 5 : Trophées
     * (simple pour l'instant, extensible ensuite)
     * ---------------------- */
    private void appliquerReglesTrophees() {
        for (CarteTrophee t : trophees) {
            // À compléter selon les variantes
            // Ex : "Meilleur 3" → +X points
        }
    }
}
