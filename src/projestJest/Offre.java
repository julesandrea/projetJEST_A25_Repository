package projestJest;

import projestJest.Carte.*;

import java.io.Serializable;

/**
 * Représente l'offre d'un joueur, composée d'une carte visible et d'une carte cachée.
 */
public class Offre implements Serializable {

    private Carte faceVisible;
    private Carte faceCachee;

    /**
     * Crée une offre à partir d'une carte visible et d'une carte cachée.
     * @param faceVisible La carte exposée.
     * @param faceCachee La carte face cachée.
     * @throws IllegalArgumentException Si l'une des cartes est nulle.
     */
    public Offre(Carte faceVisible, Carte faceCachee) {
        if (faceVisible == null || faceCachee == null) {
            throw new IllegalArgumentException("Une offre doit toujours contenir deux cartes.");
        }
        this.faceVisible = faceVisible;
        this.faceCachee = faceCachee;
    }

    /**
     * @return Vrai si l'offre contient encore ses deux cartes, faux sinon.
     */
    public boolean estComplete() {
        return faceVisible != null && faceCachee != null;
    }

    /**
     * @return La carte visible (sans la retirer).
     */
    public Carte getFaceVisible() {
        return faceVisible;
    }

    /**
     * @return La carte cachée (sans la retirer).
     */
    public Carte getFaceCachee() {
        return faceCachee;
    }

    /**
     * Permet de prendre la carte visible.
     * @return La carte visible qui est retirée de l'offre.
     * @throws IllegalStateException Si la carte est déjà prise.
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
     * Permet de prendre la carte cachée.
     * @return La carte cachée qui est retirée de l'offre.
     * @throws IllegalStateException Si la carte est déjà prise.
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
     * Prend une carte de l'offre selon le choix (visible ou cachée).
     * @param prendreVisible true pour prendre la visible, false pour la cachée.
     * @return La carte prise.
     */
    public Carte prendre(boolean prendreVisible) {
        return prendreVisible ? prendreVisible() : prendreCachee();
    }

    
    public String toString() {
        String visible = (faceVisible == null ? "X" : faceVisible.toString());
        String cachee  = (faceCachee == null ? "X" : "???");
        return "[Visible: " + visible + " | Cachée: " + cachee + "]";
    }
}
