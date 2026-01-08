package projestJest.Strategie;

import projestJest.*;
import projestJest.Joueur.*;
import java.util.List;
import java.util.Random;

/**
 * Stratégie basique effectuant des choix purement aléatoires.
 * Utile pour des bots faciles ou pour tester le flux du jeu.
 */
public class StrategieAleatoire implements Strategie {

    /**
     * Générateur de nombres aléatoires utilisé pour les décisions.
     */
    private Random rand = new Random();

    /**
     * Constructeur par défaut.
     */
    public StrategieAleatoire() {
    }

    @Override
    public boolean choisirVisibleOuCachee(Offre offre) {
        return rand.nextBoolean();
    }

    @Override
    public Joueur choisirOffre(List<Joueur> joueursValides) {
        return joueursValides.get(rand.nextInt(joueursValides.size()));
    }
}
