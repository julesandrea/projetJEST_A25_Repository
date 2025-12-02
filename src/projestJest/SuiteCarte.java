package projestJest;

/**
 * Représente la suite d'une carte (couleur).
 * La force permet de départager les égalités entre cartes visibles
 * pendant la phase de prise de cartes.
 *
 * Ordre officiel dans Jest :
 * PIQUE > TREFLE > CARREAU > COEUR
 */
public enum SuiteCarte {
    PIQUE(4),
    TREFLE(3),
    CARREAU(2),
    COEUR(1);

    /** Force de la couleur : plus le nombre est grand, plus la suite est forte */
    private final int force;

    SuiteCarte(int force) {
        this.force = force;
    }

    /** Renvoie la force de la suite */
    public int getForce() {
        return force;
    }

    /**
     * Compare deux suites selon la hiérarchie Jest.
     * @return un entier positif si a > b ; négatif si a < b ; 0 si égalité.
     */
    public static int compare(SuiteCarte a, SuiteCarte b) {
        return Integer.compare(a.force, b.force);
    }
}