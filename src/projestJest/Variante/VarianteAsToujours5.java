package projestJest.Variante;

import projestJest.*;

/**
 * Variante où les As valent toujours 5, peu importe s'il y en a d'autres de la même couleur.
 */
public class VarianteAsToujours5 implements Variante {

    @Override
    public void appliquerReglesDeScore(CompteurScore compteur) {
        compteur.setAsToujours5(true);
    }

    @Override
    public String getNom() {
        return "As valent toujours 5";
    }
}
