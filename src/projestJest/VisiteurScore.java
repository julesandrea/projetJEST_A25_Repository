package projestJest;

import projestJest.Carte.*;

public interface VisiteurScore {
    void visiter(CarteSuite carte);
    void visiter(CarteJoker carte);
    void visiter(CarteTrophee carte);
    void visiter(CarteMage carte);
    void visiter(CarteExtension carte); // Pour être générique
}
