package projestJest;

/**
 * Énumération représentant la "suite" (ou la couleur) d'une carte à jouer dans le jeu JEST.
 * Chaque suite possède une force intrinsèque utilisée pour départager les égalités lors de la détermination
 * de l'ordre de jeu (celui qui a la plus forte carte visible joue en dernier).
 *
 * L'ordre hiérarchique défini est : PIQUE > TRÈFLE > CARREAU > CŒUR.
 */
public enum SuiteCarte {
    
    /** Suite Pique (La plus forte). */
    PIQUE(4),
    
    /** Suite Trèfle. */
    TREFLE(3),
    
    /** Suite Carreau. */
    CARREAU(2),
    
    /** Suite Cœur (La plus faible). */
    COEUR(1);

    /** 
     * Valeur numérique représentant la force de la couleur. 
     * Une valeur plus élevée indique une couleur plus forte.
     */
    private final int force;

    /**
     * Constructeur privé de l'énumération.
     * 
     * @param force La force associée à la suite.
     */
    SuiteCarte(int force) {
        this.force = force;
    }

    /** 
     * Retourne la force numérique de la suite.
     * 
     * @return La force (1 à 4).
     */
    public int getForce() {
        return force;
    }

    /**
     * Compare deux suites en fonction de leur force.
     * 
     * @param a La première suite.
     * @param b La seconde suite.
     * @return Un entier positif si a &gt; b, négatif si a &lt; b, 0 si égalité.
     */
    public static int compare(SuiteCarte a, SuiteCarte b) {
        return Integer.compare(a.force, b.force);
    }
}
