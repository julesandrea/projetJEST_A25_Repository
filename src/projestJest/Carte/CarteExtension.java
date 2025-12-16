package projestJest.Carte;

import projestJest.VisiteurScore;

/**
 * Représente une carte d'extension générique avec une règle spéciale.
 */
public class CarteExtension extends Carte {

    private String regleSpeciale;

    /**
     * Constructeur pour une carte d'extension.
     * @param regleSpeciale Description de la règle spéciale de la carte.
     */
    public CarteExtension(String regleSpeciale) {
        super(null, null); 
        this.regleSpeciale = regleSpeciale;
    }

    /**
     * @return La règle spéciale associée à cette carte.
     */
    public String getRegleSpeciale() {
        return regleSpeciale;
    }

    
    public void accepter(VisiteurScore visiteur) {
        visiteur.visiter(this);
    }

    
    public String toString() {
        return "Extension [" + regleSpeciale + "]";
    }
}
