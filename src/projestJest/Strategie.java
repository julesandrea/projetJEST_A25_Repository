package projestJest;

import java.util.List;

public interface Strategie {

    /** Décider si la carte visible doit être prise */
    boolean choisirVisibleOuCachee(Offre offre);

    /** Décider chez quel joueur prendre une carte */
    Joueur choisirOffre(List<Joueur> joueursValides);
}
