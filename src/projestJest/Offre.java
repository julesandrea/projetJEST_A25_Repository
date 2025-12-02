package projestJest;

public class Offre {

    private Carte faceVisible;
    private Carte faceCachee;

    /**
     * Crée une offre à partir d'une carte visible et d'une carte cachée.
     */
    public Offre(Carte faceVisible, Carte faceCachee) {
        if (faceVisible == null || faceCachee == null) {
            throw new IllegalArgumentException("Une offre doit toujours contenir deux cartes.");
        }
        this.faceVisible = faceVisible;
        this.faceCachee = faceCachee;
    }

    /** Renvoie true si les deux cartes sont encore présentes */
    public boolean estComplete() {
        return faceVisible != null && faceCachee != null;
    }

    /** Renvoie la carte visible (sans la retirer) */
    public Carte getFaceVisible() {
        return faceVisible;
    }

    /** Renvoie la carte cachée (sans la retirer) — juste pour la logique, pas pour l'affichage */
    public Carte getFaceCachee() {
        return faceCachee;
    }

    /**
     * Permet de prendre la carte visible.
     * Retire la carte de l'offre et la renvoie.
     */
    public Carte prendreVisible() {
        if (faceVisible == null) {
            throw new IllegalStateException("La carte visible a déjà été prise !");
        }
        Carte temp = faceVisible;
        faceVisible = null;
        return temp;
    }

    /**
     * Permet à un joueur de prendre la carte cachée SANS pouvoir la regarder avant.
     * Retire la carte de l'offre et la renvoie.
     */
    public Carte prendreCachee() {
        if (faceCachee == null) {
            throw new IllegalStateException("La carte cachée a déjà été prise !");
        }
        Carte temp = faceCachee;
        faceCachee = null;
        return temp;
    }

    /**
     * Méthode pratique : le joueur choisit visible ou cachée.
     * @param prendreVisible true = visible ; false = cachée
     */
    public Carte prendre(boolean prendreVisible) {
        return prendreVisible ? prendreVisible() : prendreCachee();
    }

    /** Affichage utile pour le mode console */
    @Override
    public String toString() {
        String visible = (faceVisible == null ? "X" : faceVisible.toString());
        String cachee  = (faceCachee == null ? "X" : "???"); // On ne montre jamais la carte cachée
        return "[Visible: " + visible + " | Cachée: " + cachee + "]";
    }
}