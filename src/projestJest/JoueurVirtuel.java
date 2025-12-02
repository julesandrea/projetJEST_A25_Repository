package projestJest;

import java.util.List;

public class JoueurVirtuel extends Joueur {

    public JoueurVirtuel(String nom, Strategie strategie) {
        super(nom);
        this.strategie = strategie;
    }

    @Override
    public void faireOffre(Carte c1, Carte c2) {
        // Simple : on met la première visible
        this.offre = new Offre(c1, c2);
        System.out.println(nom + " (IA) a fait son offre : " + offre);
    }

    @Override
    public Carte choisirCarte(Offre offre) {
        // Délègue à la stratégie
        boolean prendreVisible = strategie.choisirVisibleOuCachee(offre);
        Carte retiree = offre.prendre(prendreVisible);

        System.out.println(nom + " (IA) a pris : " + retiree);
        return retiree;
    }

    @Override
    public Joueur choisirJoueurCible(List<Joueur> joueursValides) {
        return strategie.choisirOffre(joueursValides);
    }
}