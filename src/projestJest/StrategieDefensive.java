package projestJest;

import java.util.List;
import java.util.Random;

public class StrategieDefensive implements Strategie {

    private Random rand = new Random();

    @Override
    public boolean choisirVisibleOuCachee(Offre offre) {

        Carte visible = offre.getFaceVisible();
        SuiteCarte couleur = visible.getSuite();
        int valeur = visible.getValeur().getFaceValue();

        // === 1. ÉVITER les carreaux à tout prix ===
        if (couleur == SuiteCarte.CARREAU)
            return false; // on prend la cachée

        // === 2. Méfiance envers les cœurs sans Joker ===
        if (couleur == SuiteCarte.COEUR && valeur >= 2)
            return false;

        // === 3. Une carte visible très forte peut être dangereuse pour les autres → on la prend
        if (valeur == 4 || valeur == 3)
            return true;

        // === 4. Si visible est neutre (petite noire), on hésite ===
        if (couleur == SuiteCarte.PIQUE || couleur == SuiteCarte.TREFLE) {
            if (valeur <= 2)
                return rand.nextInt(100) < 40; // tendance défensive : 60% cachée
        }

        // === 5. AS visible = potentiellement risqué (si tu n’as pas la couleur)
        if (visible.getValeur().estAs())
            return false; // décision prudente : on prend la cachée

        // === 6. Comportement par défaut très défensif ===
        return false;
    }

    @Override
    public Joueur choisirOffre(List<Joueur> joueursValides) {

        Joueur meilleur = null;
        int plusDangereux = Integer.MIN_VALUE;

        for (Joueur j : joueursValides) {

            Carte visible = j.getOffre().getFaceVisible();
            int danger = evaluerDanger(visible);

            // On choisit l’adversaire dont la carte visible est la PLUS dangereuse
            // (car en stratégie défensive, on veut empêcher les autres de la prendre)
            if (danger > plusDangereux) {
                plusDangereux = danger;
                meilleur = j;
            }
        }

        return meilleur;
    }

    /**
     * Évalue le danger d'une carte : plus la carte est dangereuse pour la suite du jeu,
     * plus le joueur défensif souhaite la neutraliser.
     */
    private int evaluerDanger(Carte c) {

        int danger = 0;

        int valeur = c.getValeur().getFaceValue();
        SuiteCarte couleur = c.getSuite();

        // --- valeur brute ---
        danger += valeur;

        // --- couleurs dangereuses pour la défense ---
        if (couleur == SuiteCarte.PIQUE || couleur == SuiteCarte.TREFLE)
            danger += 3; // fortes et positives → risquées si d'autres les prennent

        // --- carreaux sont dangereux si d'autres peuvent les utiliser pour blocages ---
        if (couleur == SuiteCarte.CARREAU)
            danger += 1; // danger faible mais présent

        // --- cœur moyen/fort est dangereux pour un joueur sans Joker ---
        if (couleur == SuiteCarte.COEUR && valeur >= 2)
            danger += 2;

        // --- As est TOUJOURS dangereux (peut valoir 5) ---
        if (c.getValeur().estAs())
            danger += 4;

        return danger;
    }
}