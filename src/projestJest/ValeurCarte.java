package projestJest;

/**
 * Énumération représentant la valeur faciale (rang) d'une carte dans le jeu JEST.
 * Cette valeur est utilisée principalement pour :
 * - Déterminer l'ordre de jeu (la valeur la plus haute joue en dernier).
 * - Calculer les scores de base (avant application des bonus/malus complexes).
 * 
 * Note : La valeur faciale n'est pas toujours équivalente aux points de victoire finaux (ex: As).
 */
public enum ValeurCarte {
    
    /** L'As, valeur faciale 1. Peut valoir 5 points dans le Jest sous conditions. */
    AS(1), 
    
    /** Le Deux, valeur faciale 2. */
    DEUX(2), 
    
    /** Le Trois, valeur faciale 3. */
    TROIS(3), 
    
    /** Le Quatre, valeur faciale 4. */
    QUATRE(4), 
    
    /** Le Six, réservé aux jokers ou cartes spéciales comme le Coeur Brisant (si utilisé comme tel). */
    SIX(6), 
    
    /** Le Joker (Bestiole), valeur faciale 0. */
    JOKER(0);

    /**
     * La valeur numérique brute de la carte.
     */
    private final int faceValue;

    /**
     * Constructeur privé de l'énumération.
     * 
     * @param faceValue La valeur numérique associée.
     */
    ValeurCarte(int faceValue) {
        this.faceValue = faceValue;
    }

    /**
     * Retourne la valeur faciale de la carte.
     * 
     * @return La valeur entière associée (ex: 1 pour As, 0 pour Joker).
     */
    public int getFaceValue() {
        return faceValue;
    }

    /**
     * Vérifie si cette valeur correspond à un As.
     * Utile pour les règles spécifiques de scoring des As.
     * 
     * @return true si c'est un AS, false sinon.
     */
    public boolean estAs() {
        return this == AS;
    }

    /**
     * Vérifie si cette valeur correspond au Joker.
     * 
     * @return true si c'est un JOKER, false sinon.
     */
    public boolean estJoker() {
        return this == JOKER;
    }
}
