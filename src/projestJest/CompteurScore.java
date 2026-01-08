package projestJest;

import projestJest.Carte.*;
import java.util.ArrayList;
import java.util.List;

/**
 * La classe CompteurScore implémente l'interface VisiteurScore selon le patron de conception Visiteur.
 * Elle est responsable du calcul du score final d'un joueur en parcourant l'ensemble de ses cartes (Jest).
 * 
 * Le calcul s'effectue en accumulant les points selon les règles de base du jeu et les variantes activées
 * (ex: As valent 5, Coeurs non négatifs...).
 */
public class CompteurScore implements VisiteurScore {

    /**
     * Constructeur par défaut du CompteurScore.
     */
    public CompteurScore() {
    }

    /**
     * Liste temporaire stockant les cartes de suite visitées pour le calcul.
     */
    private List<CarteSuite> cartesSuite = new ArrayList<>();

    /**
     * Liste temporaire stockant les trophées visités.
     */
    private List<CarteTrophee> trophees = new ArrayList<>();

    /**
     * Référence vers le Joker s'il est présent dans le Jest.
     */
    private CarteJoker joker = null;

    /**
     * Score temporaire accumulé au cours du calcul.
     */
    private int scoreTemp = 0;

    /**
     * Indicateur de la variante : si vrai, les As valent 5 points au lieu de 1 s'ils sont uniques dans leur couleur.
     */
    private boolean asToujours5 = false;

    /**
     * Indicateur de la variante : si vrai, les Coeurs ne peuvent pas réduire le score (total positif ou nul).
     */
    private boolean coeursJamaisNegatifs = false;

    /**
     * Indicateur de présence de la carte Mage (Extension).
     */
    private boolean magePresent = false;

    /**
     * Active ou désactive la règle alternative pour les As.
     * 
     * @param b true pour activer la règle (As = 5), false pour la règle standard.
     */
    public void setAsToujours5(boolean b) {
        this.asToujours5 = b;
    }

    /**
     * Active ou désactive la règle alternative pour les Coeurs.
     * 
     * @param b true pour activer la règle (Coeurs jamais négatifs), false pour la règle standard.
     */
    public void setCoeursJamaisNegatifs(boolean b) {
        this.coeursJamaisNegatifs = b;
    }

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

    @Override
    public void visiter(CarteMage carte) {
        this.magePresent = true;
        this.scoreTemp += 2; 
    }

    @Override
    public void visiter(CarteExtension carte) {
        
    }

    /**
     * Exécute le calcul complet du score en appliquant successivement toutes les règles du jeu.
     * (Couleurs, As, Joker, Paires Noires, Trophées).
     * 
     * @return Le score total final calculé.
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

    /**
     * Applique les règles de base liées aux couleurs des cartes.
     * - Pique et Trèfle : Ajoutent leur valeur faciale.
     * - Carreau : Retranchent leur valeur faciale.
     * - Coeur : Ne comptent pas directement ici (traités avec le Joker).
     */
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

    /**
     * Applique les règles spécifiques aux As.
     * Un As vaut 1 point par défaut, mais peut valoir 5 points s'il est le seul de sa couleur
     * (ou toujours si la variante est active).
     */
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

    /**
     * Applique les règles liées au Joker (Bestiole).
     * Le Joker transforme les coeurs en points positifs s'il les possède, ou s'il n'a aucun coeur (+4).
     * Sinon, les coeurs sont généralement négatifs.
     */
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

    /**
     * Applique la règle des paires noires.
     * Une paire constituée d'une carte noire (Pique/Trèfle) de même rang rapporte un bonus de 2 points.
     */
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
    
    /**
     * Applique les règles liées aux trophées et extensions spéciales (ex: Coeur Brisant).
     */
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
            }
        }
    }
}
