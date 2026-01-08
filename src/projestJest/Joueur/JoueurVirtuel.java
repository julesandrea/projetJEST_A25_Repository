package projestJest.Joueur;

import projestJest.*;
import projestJest.Carte.*;
import projestJest.Strategie.*;
import java.util.List;

/**
 * Représente un joueur virtuel (Bot).
 * Ses décisions (choix d'offre, choix de prise, choix de cible) sont entièrement déléguées
 * à une stratégie configurable (Pattern Strategy).
 */
public class JoueurVirtuel extends Joueur {

    /**
     * Constructeur pour un joueur virtuel.
     * 
     * @param nom Le nom du bot.
     * @param strategie La stratégie de jeu qu'appliquera ce bot.
     */
    public JoueurVirtuel(String nom, Strategie strategie) {
        super(nom);
        this.strategie = strategie;
    }

    @Override
    public void faireOffre(Carte c1, Carte c2, InterfaceUtilisateur vue) {
        this.offre = new Offre(c1, c2);
        vue.afficherMessage(nom + " a fait son offre.");
    }

    @Override
    public Carte choisirCarte(Offre offre, InterfaceUtilisateur vue) {
        boolean prendreVisible = strategie.choisirVisibleOuCachee(offre);
        Carte retiree = offre.prendre(prendreVisible);

        vue.afficherMessage(nom + "  a pris : " + retiree);
        vue.afficherMessage(nom + "  possède maintenant dans son Jest : " + this.jest);
        return retiree;
    }

    @Override
    public Joueur choisirJoueurCible(List<Joueur> joueursValides, InterfaceUtilisateur vue) {
        return strategie.choisirOffre(joueursValides);
    }
}
