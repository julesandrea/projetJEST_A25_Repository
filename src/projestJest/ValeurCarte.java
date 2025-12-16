package projestJest;

/**
 * Représente la valeur brute (face value) d'une carte.
 * Attention : ce n'est PAS le score final dans le Jest !
 */
public enum ValeurCarte {
    AS(1), 
    DEUX(2), 
    TROIS(3), 
    QUATRE(4), 
    SIX(6), 
    JOKER(0);

    /** Valeur brute, utilisée pour comparer les cartes dans les tours */
    private final int faceValue;

    ValeurCarte(int faceValue) {
        this.faceValue = faceValue;
    }

    /**
     * Renvoie la valeur brute de la carte.
     * - As vaut 1 ici (il deviendra 5 dans le Visitor si c'est sa seule carte de la couleur)
     * - Joker vaut 0
     */
    public int getFaceValue() {
        return faceValue;
    }

    /** Indique si cette valeur correspond à un As */
    public boolean estAs() {
        return this == AS;
    }

    /** Indique si cette valeur correspond au Joker */
    public boolean estJoker() {
        return this == JOKER;
    }
}
