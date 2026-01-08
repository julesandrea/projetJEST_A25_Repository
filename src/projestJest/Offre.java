package projestJest;

import projestJest.Carte.*;

import java.io.Serializable;

/**
 * La classe Offre représente la main temporaire d'un joueur durant un tour, constituée de deux cartes.
 * L'une des cartes est exposée face visible (publique) tandis que l'autre reste face cachée (privée).
 * Les autres joueurs (ou le joueur lui-même) peuvent ensuite choisir de prendre l'une de ces deux cartes.
 */
public class Offre implements Serializable {

    /**
     * La carte exposée face visible.
     */
    private Carte faceVisible;

    /**
     * La carte gardée face cachée.
     */
    private Carte faceCachee;

    /**
     * Construit une nouvelle Offre avec une carte visible et une carte cachée.
     * 
     * @param faceVisible La carte à exposer.
     * @param faceCachee La carte à cacher.
     * @throws IllegalArgumentException Si l'une des deux cartes est nulle.
     */
    public Offre(Carte faceVisible, Carte faceCachee) {
        if (faceVisible == null || faceCachee == null) {
            throw new IllegalArgumentException("Une offre doit toujours contenir deux cartes.");
        }
        this.faceVisible = faceVisible;
        this.faceCachee = faceCachee;
    }

    /**
     * Vérifie si l'offre contient encore ses deux cartes (n'a pas encore été entamée).
     * 
     * @return true si les deux cartes sont présentes, false si l'une a été prise.
     */
    public boolean estComplete() {
        return faceVisible != null && faceCachee != null;
    }

    /**
     * Accesseur pour consulter la carte visible sans la retirer de l'offre.
     * 
     * @return La carte visible, ou null si elle a déjà été prise.
     */
    public Carte getFaceVisible() {
        return faceVisible;
    }

    /**
     * Accesseur pour consulter la carte cachée sans la retirer de l'offre.
     * 
     * @return La carte cachée, ou null si elle a déjà été prise.
     */
    public Carte getFaceCachee() {
        return faceCachee;
    }

    /**
     * Retire et retourne la carte visible de l'offre.
     * Cette action modifie l'état de l'offre en supprimant la carte.
     * 
     * @return La carte qui était visible.
     * @throws IllegalStateException Si la carte visible a déjà été prise.
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
     * Retire et retourne la carte cachée de l'offre.
     * Cette action modifie l'état de l'offre en supprimant la carte.
     * 
     * @return La carte qui était cachée.
     * @throws IllegalStateException Si la carte cachée a déjà été prise.
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
     * Méthode utilitaire pour prendre une carte spécifiée par un booléen.
     * 
     * @param prendreVisible Si true, tente de prendre la carte visible. Si false, tente de prendre la carte cachée.
     * @return La carte prise.
     */
    public Carte prendre(boolean prendreVisible) {
        return prendreVisible ? prendreVisible() : prendreCachee();
    }

    @Override
    public String toString() {
        String visible = (faceVisible == null ? "X" : faceVisible.toString());
        String cachee  = (faceCachee == null ? "X" : "???");
        return "[Visible: " + visible + " | Cachée: " + cachee + "]";
    }
}
