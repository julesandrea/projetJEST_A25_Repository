package projestJest;

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
