package projestJest.Carte;

import projestJest.VisiteurScore;

/**
 * Représente une carte d'extension générique.
 * Ces cartes introduisent généralement une règle spéciale modifiant le gameplay ou le score.
 */
public class CarteExtension extends Carte {

    /**
     * Description textuelle de la règle spéciale apportée par cette carte.
     */
    private String regleSpeciale;

    /**
     * Constructeur pour une carte d'extension.
     * 
     * @param regleSpeciale Description de la règle spéciale de la carte.
     */
    public CarteExtension(String regleSpeciale) {
        super(null, null); 
        this.regleSpeciale = regleSpeciale;
    }

    /**
     * Retourne la description de la règle spéciale.
     * 
     * @return La règle spéciale associée à cette carte.
     */
    public String getRegleSpeciale() {
        return regleSpeciale;
    }

    @Override
    public void accepter(VisiteurScore visiteur) {
        visiteur.visiter(this);
    }

    @Override
    public String toString() {
        return "Extension [" + regleSpeciale + "]";
    }
}
