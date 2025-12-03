package projestJest;

public class VarianteCoeursJamaisNegatifs implements Variante {

    @Override
    public void appliquerReglesDeScore(CompteurScore compteur) {
        compteur.setCoeursJamaisNegatifs(true);
    }

    @Override
    public String getNom() {
        return "Cœurs jamais négatifs";
    }
}
