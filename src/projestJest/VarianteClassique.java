package projestJest;

public class VarianteClassique implements Variante {

    @Override
    public void appliquerReglesDeScore(CompteurScore compteur) {
        // ne change rien
    }

    @Override
    public String getNom() {
        return "Règles classiques";
    }
}
