package projestJest;

import java.util.List;
import java.util.Random;

public class StrategieAleatoire implements Strategie {

    private Random rand = new Random();

    @Override
    public boolean choisirVisibleOuCachee(Offre offre) {
        return rand.nextBoolean();
    }

    @Override
    public Joueur choisirOffre(List<Joueur> joueursValides) {
        return joueursValides.get(rand.nextInt(joueursValides.size()));
    }
}
