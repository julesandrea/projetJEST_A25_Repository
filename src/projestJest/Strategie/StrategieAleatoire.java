package projestJest.Strategie;

import projestJest.*;
import projestJest.Joueur.*;
import java.util.List;
import java.util.Random;

/**
 * Stratégie simple qui effectue des choix aléatoires.
 */
public class StrategieAleatoire implements Strategie {

    private Random rand = new Random();

    
    public boolean choisirVisibleOuCachee(Offre offre) {
        return rand.nextBoolean();
    }

    
    public Joueur choisirOffre(List<Joueur> joueursValides) {
        return joueursValides.get(rand.nextInt(joueursValides.size()));
    }
}
